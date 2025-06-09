package com.infy.exception;

// Exception thrown when a customer is not found in the system.

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new CustomerNotFoundException with the given message.
	 *
	 * @param message the exception message
	 */
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
