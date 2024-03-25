package ru.sergey.patseev.authorization_service.exception;

/**
 * The exception thrown when there is a data conflict during registration.
 */
public class DataAlreadyInUseException extends RuntimeException {

	/**
	 * Constructs a new DataAlreadyInUseException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public DataAlreadyInUseException(String message) {
		super(message);
	}
}
