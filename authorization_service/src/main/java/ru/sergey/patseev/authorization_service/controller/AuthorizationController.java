package ru.sergey.patseev.authorization_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sergey.patseev.authorization_service.annotation.AuthorizationRestController;
import ru.sergey.patseev.authorization_service.dto.UserDto;
import ru.sergey.patseev.authorization_service.exception.AuthorizationExceptionHandler;
import ru.sergey.patseev.authorization_service.service.AuthorizationService;

/**
 * Controller for handling user authorization and registration.
 * Marked with a custom annotation {@link AuthorizationRestController} which includes the main @RestController, @RequestMapping
 * and gets the path to the current controller as `path`.
 * Marked with a custom annotation which includes the main @RestController, @RequestMapping.
 * This was done specifically for the {@link AuthorizationExceptionHandler} class,
 * which will respond specifically to this controller.
 */
@AuthorizationRestController(path = "/api/v1/authorization")
@RequiredArgsConstructor
public class AuthorizationController {
	private static final String MESSAGE_UPON_SUCCESSFUL_REGISTRATION
			= "The user is registered. To confirm your email, go to the email address you provided during registration.";
	private static final String MESSAGE_UPON_SUCCESSFUL_ACTIVATION
			= "activation was successful";
	private static final String ERROR_MESSAGE = "error";

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

	/**
	 * Endpoint intended for account activation.
	 * If the user does not activate the account after registration, he will not be able to log in.
	 * In the path variables we get a unique activation code generated during registration.
	 * This code must match the code that was saved on the database side.
	 *
	 * @param activationCode Activation code obtained from path variable.
	 * @return Returns the response status in the form of a code and message.
	 */
	@GetMapping("/activation/{activationCode}")
	public ResponseEntity<?> activationAccount(@PathVariable("activationCode") String activationCode) {
		boolean activated = authorizationService.activateAccount(activationCode);
		if (activated) {
			return ResponseEntity.ok().body(MESSAGE_UPON_SUCCESSFUL_ACTIVATION);
		}
		return ResponseEntity.badRequest().body(ERROR_MESSAGE);
	}
}
