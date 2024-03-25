package ru.sergey.patseev.authorization_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sergey.patseev.authorization_service.annotation.AuthorizationRestController;

/**
 * Class responsible for handling exceptions that occur in controllers annotated with {@link AuthorizationRestController}.
 */
@ControllerAdvice(annotations = AuthorizationRestController.class)
public class AuthorizationExceptionHandler {

	/**
	 * Exception handler for {@link UserNotFoundException}.
	 *
	 * @param e The exception type {@link UserNotFoundException}.
	 * @return ResponseEntity with a bad request status and the exception message in the response body.
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	/**
	 * Exception handler for {@link AccountActivatedException}.
	 *
	 * @param e The exception type {@link AccountActivatedException}.
	 * @return ResponseEntity with a bad request status and the exception message in the response body.
	 */
	@ExceptionHandler(AccountActivatedException.class)
	public ResponseEntity<?> accountActivatedExceptionHandler(AccountActivatedException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	/**
	 * Exception handler for {@link DataAlreadyInUseException}.
	 *
	 * @param e The exception type {@link DataAlreadyInUseException}.
	 * @return ResponseEntity with a conflict status and the exception message in the response body.
	 */
	@ExceptionHandler(DataAlreadyInUseException.class)
	public ResponseEntity<?> dataAlreadyInUseExceptionHandler(DataAlreadyInUseException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
}
