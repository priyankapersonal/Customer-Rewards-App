package com.infy.exception;

// Represents the structure of error response returned to the client.

public class ErrorDetails {

	private int statusCode;
	private String message;
	private String details;

	/**
	 * Constructs an ErrorDetails instance.
	 *
	 * @param statusCode the HTTP status code
	 * @param message    the error message
	 * @param details    additional details (e.g., request path)
	 */
	public ErrorDetails(int statusCode, String message, String details) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.details = details;
	}

	/**
	 * Gets the HTTP status code.
	 *
	 * @return status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the HTTP status code.
	 *
	 * @param statusCode the new status code
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Gets the error message.
	 *
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the error message.
	 *
	 * @param message the error message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets additional error details.
	 *
	 * @return details string
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets additional error details.
	 *
	 * @param details the details string
	 */
	public void setDetails(String details) {
		this.details = details;
	}
}
