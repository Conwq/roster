package ru.sergey.patseev.authorization_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sergey.patseev.authorization_service.dto.UserDto;
import ru.sergey.patseev.authorization_service.exception.AccountActivatedException;
import ru.sergey.patseev.authorization_service.exception.DataAlreadyInUseException;
import ru.sergey.patseev.authorization_service.exception.UserNotFoundException;
import ru.sergey.patseev.authorization_service.mapper.UserMapper;
import ru.sergey.patseev.authorization_service.model.RoleEntity;
import ru.sergey.patseev.authorization_service.model.RoleEnum;
import ru.sergey.patseev.authorization_service.model.UserDetailEntity;
import ru.sergey.patseev.authorization_service.model.UserEntity;
import ru.sergey.patseev.authorization_service.repository.RoleRepository;
import ru.sergey.patseev.authorization_service.repository.UserDetailRepository;
import ru.sergey.patseev.authorization_service.repository.UserRepository;
import ru.sergey.patseev.authorization_service.service.AuthorizationService;
import ru.sergey.patseev.jwtservicespringbootstrarter.constant.JwtClaimName;
import ru.sergey.patseev.jwtservicespringbootstrarter.service.JwtService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

//todo javadoc
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final JwtService jwtService;
	private final UserDetailRepository userDetailRepository;

	/**
	 * Registers a new user based on provided user data.
	 *
	 * @param userDto Data of the user to be registered.
	 * @return True if the user was successfully registered, false otherwise.
	 * @throws DataAlreadyInUseException if the provided user data is already in use.
	 */
	@Override
	@Transactional
	public boolean registerNewUser(UserDto userDto) {
		boolean dataIsExist = checkDataExistence(userDto);
		if (dataIsExist) {
			throw new DataAlreadyInUseException("Data already in use");
		}
		UserEntity userEntity = createUserEntity(userDto);
		Long generatedUserId = userRepository.save(userEntity).getUserId();
		UserDetailEntity userDetailEntity = createUserDetail(userEntity, generatedUserId);
		userDetailRepository.save(userDetailEntity);

		//todo send email to the email taken from userDto

		return true;
	}

	/**
	 * Authorizes a user based on provided user data.
	 *
	 * @param userDto Data of the user to be authorized.
	 * @return JWT token if the user is successfully authorized.
	 * @throws UserNotFoundException     if the user is not found or the provided data is incorrect.
	 * @throws AccountActivatedException if the user account is not activated.
	 */
	@Override
	@Transactional
	public String authorizeUser(UserDto userDto) {
		UserEntity user = userRepository.findByUsername(userDto.username())
				.orElseThrow(() -> new UserNotFoundException("Wrong data"));
		if (!checkPasswordMatches(user, userDto)) {
			throw new UserNotFoundException("Wrong data");
		}
		if (!user.isActivated()) {
			throw new AccountActivatedException("Account is not active");
		}
		updateLastLoginDate(user);
		Map<String, Object> extraClaims = createExtraClaims(user);
		return jwtService.generateToken(extraClaims, user.getUsername());
	}

	/**
	 * Activates a user account using activation code.
	 *
	 * @param activationCode Activation code sent to the user.
	 * @return True if the account was successfully activated.
	 * @throws UserNotFoundException     if the user is not found or the activation code is incorrect.
	 * @throws AccountActivatedException if the user account has already been activated.
	 */
	@Override
	@Transactional
	public boolean activateAccount(String activationCode) {
		UserEntity userEntity = userRepository.findByActivationCode(activationCode)
				.orElseThrow(() -> new UserNotFoundException("wrong data"));
		if (userEntity.isActivated()) {
			throw new AccountActivatedException("account has already been activated");
		}
		userEntity.setActivated(true);
		userEntity.setActivationCode(null);
		userRepository.save(userEntity);
		return true;
	}

	/**
	 * Checks for the existence of user data.
	 *
	 * @param userDto Data to check for existence.
	 * @return Returns true if some or all of the passed data exists, false otherwise.
	 */
	private boolean checkDataExistence(UserDto userDto) {
		Optional<UserEntity> optionalEmail = userRepository.findByEmail(userDto.email());
		Optional<UserEntity> optionalUsername = userRepository.findByUsername(userDto.username());
		return optionalEmail.isPresent() || optionalUsername.isPresent();
	}

	/**
	 * Creates a UserEntity instance based on provided UserDto.
	 *
	 * @param userDto Data of the user to create entity from.
	 * @return Created UserEntity instance.
	 */
	private UserEntity createUserEntity(UserDto userDto) {
		UserEntity userEntity = userMapper.toEntity(userDto);
		RoleEntity role = roleRepository.getRoleEntityByRoleName(RoleEnum.USER.name());
		role.getUsers().add(userEntity);
		userEntity.getRoles().add(role);
		userEntity.setActivationCode(generateActivationCode());
		userEntity.setActivated(false);
		return userEntity;
	}

	/**
	 * Generates an activation code for user account activation.
	 *
	 * @return Generated activation code.
	 */
	private String generateActivationCode() {
		String activationCode = UUID.randomUUID().toString();
		while (userRepository.findByActivationCode(activationCode).isPresent()) {
			activationCode = UUID.randomUUID().toString();
		}
		return activationCode;
	}

	/**
	 * Creates a UserDetailEntity instance for the provided UserEntity and userId.
	 *
	 * @param userEntity UserEntity for which to create UserDetailEntity.
	 * @param userId     Generated user ID.
	 * @return Created UserDetailEntity instance.
	 */
	private UserDetailEntity createUserDetail(UserEntity userEntity, Long userId) {
		UserDetailEntity userDetailEntity = new UserDetailEntity();
		userDetailEntity.setUserId(userId);
		userDetailEntity.setUserEntity(userEntity);
		return userDetailEntity;
	}

	/**
	 * Updates the last login date for the provided user.
	 *
	 * @param user User for which to update the last login date.
	 */
	private void updateLastLoginDate(UserEntity user) {
		user.getUserDetailEntity()
				.setLastLoginAt(Timestamp.from(Instant.now()));
	}

	/**
	 * Creates extra JWT claims for the provided user.
	 *
	 * @param user User for which to create extra claims.
	 * @return Map containing extra JWT claims.
	 */
	private Map<String, Object> createExtraClaims(UserEntity user) {
		Map<String, Object> extraClaims = new HashMap<>();
		List<String> roles = this.extractUserRoles(user);
		extraClaims.put(JwtClaimName.ROLES, roles);
		extraClaims.put(JwtClaimName.USER_ID, user.getUserId());
		return extraClaims;
	}

	/**
	 * Extracts the roles associated with the given user.
	 *
	 * @param user The user entity from which to extract roles.
	 * @return List of role names associated with the user.
	 */
	private List<String> extractUserRoles(UserEntity user) {
		return user.getRoles()
				.stream()
				.map(RoleEntity::getRoleName)
				.collect(Collectors.toList());
	}

	/**
	 * Checks if the password provided in the UserDto matches the password stored in the UserEntity.
	 *
	 * @param userEntity The UserEntity containing the stored password.
	 * @param userDto    The UserDto containing the password to check.
	 * @return True if the passwords match, false otherwise.
	 */
	private boolean checkPasswordMatches(UserEntity userEntity, UserDto userDto) {
		String userPassword = userEntity.getPassword();
		return userPassword.equals(userDto.password());
	}
}
