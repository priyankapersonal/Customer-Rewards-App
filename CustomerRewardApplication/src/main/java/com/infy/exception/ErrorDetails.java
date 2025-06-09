package com.infy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the structure of the error response returned to the client.
 */
@Data
@AllArgsConstructor
public class ErrorDetails {

    private int statusCode;
    private String message;
    private String details;
}
