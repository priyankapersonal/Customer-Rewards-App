package com.infy.exception;

/**
 * Exception thrown when a date format provided is invalid.
 */
public class InvalidDateFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new InvalidDateFormatException with the given message.
	 *
	 * @param message the exception message
	 */
	public InvalidDateFormatException(String message) {
		super(message);
	}
}
