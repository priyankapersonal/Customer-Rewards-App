package com.infy;

import com.infy.model.Customer;
import com.infy.model.Transaction;
import com.infy.repository.CustomerRepository;
import com.infy.repository.TransactionRepository;
import com.infy.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link RewardsService} using Spring context and actual
 * database.
 */
@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class RewardsServiceIntegrationTest {

	@Autowired
	private RewardsService rewardsService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private Customer customer;
	private Transaction transaction;

	/**
	 * Setup method to initialize data before each test. Clears previous test data
	 * and inserts a sample customer and transaction.
	 */
	@BeforeEach
	void setup() {
		transactionRepository.deleteAll();
		customerRepository.deleteAll();

		customer = new Customer();
		customer.setCustomerName("John");

		transaction = new Transaction();
		transaction.setAmount(120);
		transaction.setDate(LocalDate.of(2024, 1, 15));
		transaction.setCustomer(customer);

		customer.setTransaction(List.of(transaction));
	}

	/**
	 * Test saving a customer with valid data. Verifies that the customer is
	 * persisted and assigned a valid ID.
	 */
	@Test
	void testSaveCustomerSuccess() {
		Customer saved = rewardsService.saveCustomer(customer);
		assertNotNull(saved);
		assertNotNull(saved.getCustomerId());
		assertEquals("John", saved.getCustomerName());
	}

	/**
	 * Test reward calculation with valid input data. Verifies reward points are
	 * calculated and returned correctly.
	 */
	@Test
	void testCalculateRewardsSuccess() {
		Customer saved = rewardsService.saveCustomer(customer);

		Map<String, Object> result = rewardsService.calculateRewards(saved.getCustomerId(), LocalDate.of(2024, 1, 1),
				LocalDate.of(2024, 12, 31));

		assertNotNull(result);
		assertTrue(result.containsKey("Total Rewards"));
		assertEquals(saved.getCustomerName(), ((Customer) result.get("Customer Details")).getCustomerName());
	}

	/**
	 * Test reward calculation when the start date is after the end date. Verifies
	 * that an exception is thrown with an appropriate message.
	 */
	@Test
	void testCalculateRewardsInvalidDateOrder() {
		Customer saved = rewardsService.saveCustomer(customer);

		Exception ex = assertThrows(RuntimeException.class, () -> rewardsService.calculateRewards(saved.getCustomerId(),
				LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1)));
		assertTrue(ex.getMessage().contains("Start date cannot be after end date"));
	}

	/**
	 * Test reward calculation with an invalid (zero) customer ID. Verifies that an
	 * exception is thrown.
	 */
	@Test
	void testCalculateRewardsWithInvalidCustomerId() {
		Exception ex = assertThrows(RuntimeException.class,
				() -> rewardsService.calculateRewards(0L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
		assertTrue(ex.getMessage().contains("Customer ID must be a positive number"));
	}

	/**
	 * Test reward calculation with null dates. Verifies that an exception is thrown
	 * indicating date cannot be null.
	 */
	@Test
	void testCalculateRewardsWithNullDates() {
		Customer saved = rewardsService.saveCustomer(customer);

		Exception ex = assertThrows(RuntimeException.class,
				() -> rewardsService.calculateRewards(saved.getCustomerId(), null, null));
		assertTrue(ex.getMessage().contains("cannot be null"));
	}
}
