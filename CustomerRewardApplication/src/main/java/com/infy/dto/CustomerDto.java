package com.infy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for customer details and their transactions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

	@NotNull(message = "Customer name cannot be null")
	@NotBlank(message = "Customer name cannot be null or blank.")
	private String customerName;

	@NotNull(message = "Transaction list cannot be null")
	@Size(min = 1, message = "Transaction list cannot be empty")
	private List<TransactionDto> transaction;
}