package ru.sergey.patseev.authorization_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sergey.patseev.authorization_service.dto.UserDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

	@Override
	@Transactional
	public boolean registerNewUser(UserDto userDto) {
		UserEntity userEntity = createUserEntity(userDto);
		Long generatedUserId = userRepository.save(userEntity).getUserId();
		UserDetailEntity userDetailEntity = createUserDetail(userEntity, generatedUserId);
		userDetailRepository.save(userDetailEntity);
		return true;
	}

	@Override
	@Transactional
	public String authorizeUser(UserDto userDto) {
		UserEntity user = userRepository
				.findByUsername(userDto.username())
				.orElseThrow(() -> new UserNotFoundException("wrong data"));
		if (!checkPasswordMatches(user, userDto)) {
			throw new UserNotFoundException("wrong data");
		}
		updateLastLoginDate(user);
		Map<String, Object> extraClaims = createExtraClaims(user);
		return jwtService.generateToken(extraClaims, user.getUsername());
	}

	private UserEntity createUserEntity(UserDto userDto) {
		UserEntity userEntity = userMapper.toEntity(userDto);
		RoleEntity role = roleRepository.getRoleEntityByRoleName(RoleEnum.USER.name());
		role.getUsers().add(userEntity);
		userEntity.getRoles().add(role);
		userEntity.setActivationCode(UUID.randomUUID().toString());
		userEntity.setActivated(false);
		roleRepository.save(role);
		return userEntity;
	}

	private UserDetailEntity createUserDetail(UserEntity userEntity, Long userId) {
		UserDetailEntity userDetailEntity = new UserDetailEntity();
		userDetailEntity.setUserId(userId);
		userDetailEntity.setUserEntity(userEntity);
		return userDetailEntity;
	}

	private void updateLastLoginDate(UserEntity user) {
		user.getUserDetailEntity().setLastLoginAt(Timestamp.from(Instant.now()));
	}

	private Map<String, Object> createExtraClaims(UserEntity user) {
		Map<String, Object> extraClaims = new HashMap<>();
		List<String> roles = this.extractUserRoles(user);
		extraClaims.put(JwtClaimName.ROLES, roles);
		extraClaims.put(JwtClaimName.USER_ID, user.getUserId());
		return extraClaims;
	}

	private List<String> extractUserRoles(UserEntity user) {
		return user.getRoles()
				.stream()
				.map(RoleEntity::getRoleName)
				.collect(Collectors.toList());
	}

	private boolean checkPasswordMatches(UserEntity userEntity, UserDto userDto) {
		String userPassword = userEntity.getPassword();
		return userPassword.equals(userDto.password());
	}
}
