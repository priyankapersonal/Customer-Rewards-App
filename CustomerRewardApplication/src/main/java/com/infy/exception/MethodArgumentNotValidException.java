package com.infy.exception;

public class MethodArgumentNotValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new InvalidRequestException with the given message.
	 *
	 * @param message the exception message
	 */
	public MethodArgumentNotValidException(String message) {
		super(message);
	}
}
