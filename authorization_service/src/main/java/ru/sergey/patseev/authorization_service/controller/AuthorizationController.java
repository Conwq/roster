package ru.sergey.patseev.authorization_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sergey.patseev.authorization_service.dto.UserDto;
import ru.sergey.patseev.authorization_service.service.AuthorizationService;

/**
 * Controller for handling user authorization and registration.
 */
@RestController
@RequestMapping("/api/v1/authorization")
@RequiredArgsConstructor
public class AuthorizationController {
	private static final String MESSAGE_UPON_SUCCESSFUL_REGISTRATION
			= "The user is registered. To confirm your email, go to the email address you provided during registration.";
	private final AuthorizationService authorizationService;

	/**
	 * Endpoint for registering a new user.
	 *
	 * @param userDto The user DTO containing registration information.
	 * @return ResponseEntity with a message indicating successful registration or a bad request if registration failed.
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerNewUser(@RequestBody UserDto userDto) {
		boolean registeredUser = authorizationService.registerNewUser(userDto);
		if (registeredUser) {
			return ResponseEntity.ok(MESSAGE_UPON_SUCCESSFUL_REGISTRATION);
		}
		return ResponseEntity.badRequest().build();
	}

	/**
	 * Endpoint for authorizing a user and generating a JWT token.
	 *
	 * @param userDto The user DTO containing login credentials.
	 * @return ResponseEntity with the generated JWT token in the response body.
	 */
	@PostMapping("/authorize")
	public ResponseEntity<String> authorizeUser(@RequestBody UserDto userDto) {
		String jwtToken = authorizationService.authorizeUser(userDto);
		return ResponseEntity.ok().body(jwtToken);
	}
}
