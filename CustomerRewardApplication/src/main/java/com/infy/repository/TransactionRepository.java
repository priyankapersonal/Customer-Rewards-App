package com.infy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infy.model.Transaction;

/**
 * Repository interface for managing Transaction entities. Provides methods to
 * query transactions by customer and date range.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	/**
	 * Retrieves transactions for a specific customer within a date range.
	 *
	 * @param customerId the ID of the customer
	 * @param startDate  the start date (inclusive)
	 * @param endDate    the end date (inclusive)
	 * @return list of transactions matching the criteria
	 */
	List<Transaction> findByCustomerCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves all transactions for a specific customer.
	 *
	 * @param customerId the ID of the customer
	 * @return list of transactions for the customer
	 */
	List<Transaction> findByCustomerCustomerId(Long customerId);
}
