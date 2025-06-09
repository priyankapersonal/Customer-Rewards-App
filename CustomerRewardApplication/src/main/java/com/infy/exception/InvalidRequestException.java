package com.infy.exception;

/**
 * Exception thrown when a request is invalid or improperly formed.
 */
public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new InvalidRequestException with the given message.
	 *
	 * @param message the exception message
	 */
	public InvalidRequestException(String message) {
		super(message);
	}
}
