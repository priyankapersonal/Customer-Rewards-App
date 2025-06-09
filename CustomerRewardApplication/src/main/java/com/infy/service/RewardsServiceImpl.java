package com.infy.service;

import com.infy.CustomerRewardsApplication;
import com.infy.exception.CustomerNotFoundException;
import com.infy.exception.InvalidDateFormatException;
import com.infy.exception.InvalidRequestException;
import com.infy.model.Customer;
import com.infy.model.Transaction;
import com.infy.repository.CustomerRepository;
import com.infy.repository.TransactionRepository;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RewardsService}. Handles business logic for customer
 * reward management.
 */
@Service
public class RewardsServiceImpl implements RewardsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRewardsApplication.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Saves a customer and all associated transactions after validation.
	 *
	 * @param customer the customer object including transaction list
	 * @return the persisted customer entity
	 * @throws InvalidDateFormatException if any transaction date is null
	 * @throws InvalidRequestException    if any transaction amount is zero or
	 *                                    negative
	 */

	@Override
	public Customer saveCustomer(Customer customer) {
		logger.info("Saving Customer: {}", customer.getCustomerName());

		for (Transaction transaction : customer.getTransaction()) {
			if (transaction.getDate() == null) {
				logger.warn("Transaction date is null");
				throw new InvalidDateFormatException("Transaction date cannot be null.");
			}
			if (transaction.getAmount() <= 0) {
				logger.warn("Invalid transaction amount: {}", transaction.getAmount());
				throw new InvalidRequestException("Transaction amount must be greater than zero.");
			}
		}

		Customer savedCustomer = customerRepository.save(customer);

		customer.getTransaction().forEach(transaction -> {
			transaction.setCustomer(savedCustomer);
			transactionRepository.save(transaction);
		});

		logger.info("Customer saved successfully with ID: {}", savedCustomer.getCustomerId());
		return savedCustomer;
	}

	/**
	 * Calculates reward points for a given customer within the provided date range.
	 *
	 * @param customerId the ID of the customer
	 * @param startDate  the start date of the reward calculation window
	 * @param endDate    the end date of the reward calculation window
	 * @return a map containing Customer Details,Rewards Breakdown per month and
	 *         Total Rewards
	 * @throws InvalidRequestException   if input values are null or invalid
	 * @throws CustomerNotFoundException if no transactions found for the customer
	 *                                   in the range
	 */

	@Override
	public Map<String, Object> calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate) {
		logger.info("Calculating rewards for customer ID: {}", customerId);

		if (customerId == null || customerId <= 0) {
			throw new InvalidRequestException("Customer ID must be a positive number.");
		}

		if (startDate == null || endDate == null) {
			throw new InvalidRequestException("Start date and end date cannot be null.");
		}

		if (startDate.isAfter(endDate)) {
			throw new InvalidRequestException("Start date cannot be after end date.");
		}

		List<Transaction> transactions = transactionRepository.findByCustomerCustomerIdAndDateBetween(customerId,
				startDate, endDate);

		if (transactions.isEmpty()) {
			throw new CustomerNotFoundException("No transactions found for customer ID: " + customerId);
		}

		List<Map<String, Object>> rewardByMonth = transactions.stream()
				.collect(Collectors.groupingBy(t -> t.getDate().getMonth().toString(), LinkedHashMap::new,
						Collectors.summingInt(t -> calculatePoints(t.getAmount()))))
				.entrySet().stream().map(entry -> {
					Map<String, Object> map = new HashMap<>();
					map.put("month", entry.getKey());
					map.put("points", entry.getValue());
					return map;
				}).collect(Collectors.toList());

		int totalPoints = rewardByMonth.stream().mapToInt(m -> (int) m.get("points")).sum();

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found for ID: " + customerId));

		Map<String, Object> response = new HashMap<>();
		response.put("Customer Details", customer);
		response.put("Rewards Breakdown", rewardByMonth);
		response.put("Total Rewards", totalPoints);

		logger.info("Reward calculation completed for customer ID: {}", customerId);
		return response;
	}

	/**
	 * Calculates reward points for a single transaction based on the amount.
	 *
	 * @param amount the amount of the transaction
	 * @return reward points calculated from the transaction amount
	 */
	private int calculatePoints(double amount) {
		int points = 0;
		if (amount > 100)
			points += (amount - 100) * 2;
		if (amount > 50)
			points += (Math.min(amount, 100) - 50);
		return points;
	}
}