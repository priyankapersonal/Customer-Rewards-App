package com.infy.service;

import com.infy.model.Customer;

import java.time.LocalDate;
import java.util.Map;

/**
 * Interface for managing customer rewards.
 */
public interface RewardsService {

	/**
	 * Save a new customer with transactions.
	 *
	 * @param customer the customer to save
	 * @return the saved customer
	 */
	Customer saveCustomer(Customer customer);

	/**
	 * Calculate rewards for a customer within a date range.
	 *
	 * @param customerId customer ID
	 * @param startDate  start date
	 * @param endDate    end date
	 * @return rewards summary
	 */
	Map<String, Object> calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate);
}