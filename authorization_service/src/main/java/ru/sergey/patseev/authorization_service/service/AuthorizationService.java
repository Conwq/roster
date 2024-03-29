package ru.sergey.patseev.authorization_service.service;

import ru.sergey.patseev.authorization_service.dto.UserDto;

/**
 * Service interface for user authorization functionality.
 */
public interface AuthorizationService {

	/**
	 * Registers a new user with the provided user data.
	 *
	 * @param userDto The user data to register.
	 * @return true if the user was successfully registered, false otherwise.
	 */
	boolean registerNewUser(UserDto userDto);

	/**
	 * Authorizes a user with the provided user data and generates a JWT token.
	 *
	 * @param userDto The user data for authorization.
	 * @return The JWT token for the authorized user.
	 */
	String authorizeUser(UserDto userDto);

	/**
	 * Activates a user account using the activation code that was assigned during user registration.
	 *
	 * @param activationCode Unique user activation code.
	 * @return Returns true if the user account was successfully activated, false otherwise.
	 */
	boolean activateAccount(String activationCode);
}
