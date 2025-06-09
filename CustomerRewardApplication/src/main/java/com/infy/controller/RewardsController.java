package com.infy.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.infy.CustomerRewardsApplication;
import com.infy.dto.CustomerDto;
import com.infy.exception.InvalidRequestException;
import com.infy.model.Customer;
import com.infy.model.Transaction;
import com.infy.service.RewardsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing customer rewards. Provides endpoints to add new
 * customers with their transactions, and calculate reward points earned over a
 * specific time period.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRewardsApplication.class);

	@Autowired
	private RewardsService rewardsService;

	/**
	 * Endpoint to add a new customer along with transaction history.
	 *
	 * @param customerDto Data transfer object containing customer name and
	 *                    transaction list.
	 * @return ResponseEntity with saved customer details and HTTP status 201
	 *         (Created).
	 * @throws InvalidRequestException if the input is null .
	 */
	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@Valid @RequestBody(required = false) CustomerDto customerDto) {
		if (customerDto == null) {
			throw new InvalidRequestException("Customer data is missing");
		}

		Customer customer = new Customer();
		customer.setCustomerName(customerDto.getCustomerName());

		List<Transaction> customerTransactions = customerDto.getTransaction().stream().map(dto -> {
			Transaction t = new Transaction();
			t.setAmount(dto.getAmount());
			t.setDate(dto.getDate());
			t.setCustomer(customer);
			return t;
		}).collect(Collectors.toList());

		customer.setTransaction(customerTransactions);

		logger.debug("Adding Customer : {}", customer);
		Customer savedCustomer = rewardsService.saveCustomer(customer);
		logger.info("Customer added successfully : {}", savedCustomer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
	}

	/**
	 * Endpoint to calculate reward points earned by a customer within a date range.
	 *
	 * @param customerId ID of the customer whose rewards need to be calculated.
	 * @param startDate  Start date of the reward calculation period (ISO format).
	 * @param endDate    End date of the reward calculation period (ISO format).
	 * @return ResponseEntity containing the reward breakdown and customer details.
	 * @throws InvalidRequestException if input parameters are invalid.
	 */
	@GetMapping("/calculateRewards/{customerId}")
	public ResponseEntity<?> getRewards(@PathVariable Long customerId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		if (customerId == null || customerId <= 0) {
			throw new InvalidRequestException("Customer ID must be a positive number");
		}

		if (startDate.isAfter(endDate)) {
			throw new InvalidRequestException("Start date cannot be after end date.");
		}

		logger.debug("Calculating rewards for customer : {}", customerId);
		Map<String, Object> rewards = rewardsService.calculateRewards(customerId, startDate, endDate);
		logger.info("Rewards calculated successfully for customer: {}", customerId);
		return new ResponseEntity<>(rewards, HttpStatus.OK);
	}
}