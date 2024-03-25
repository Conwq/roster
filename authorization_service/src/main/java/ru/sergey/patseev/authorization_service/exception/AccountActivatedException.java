package ru.sergey.patseev.authorization_service.exception;

/**
 * Exception thrown when an error occurs during account activation.
 */
public class AccountActivatedException extends RuntimeException {

	/**
	 * Constructs a new AccountActivatedException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public AccountActivatedException(String message) {
		super(message);
	}
}
