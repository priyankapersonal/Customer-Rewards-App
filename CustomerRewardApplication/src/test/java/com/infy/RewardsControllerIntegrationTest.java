package com.infy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.CustomerDto;
import com.infy.dto.TransactionDto;
import com.infy.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the RewardsController.
 * 
 * Verifies REST API behavior using a full Spring context with actual
 * request/response flow.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
public class RewardsControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private CustomerDto customerDto;

	// Initializes test data before each test.

	@BeforeEach
	void setup() {
		customerDto = new CustomerDto();
		customerDto.setCustomerName("Sam");

		TransactionDto transaction = new TransactionDto();
		transaction.setAmount(120.0);
		transaction.setDate(LocalDate.of(2024, 4, 15));
		customerDto.setTransaction(List.of(transaction));
	}

	// Tests successful creation of a customer with valid transaction.

	@Test
	void testCreateCustomerSuccess() throws Exception {
		mockMvc.perform(post("/api/rewards/addCustomer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerName").value("Sam"));
	}

	// Tests error response when customer name is blank.

	@Test
	void testCreateCustomerBlankName() throws Exception {
		customerDto.setCustomerName("   ");

		mockMvc.perform(post("/api/rewards/addCustomer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Customer name cannot be null or blank.")));
	}

	// Tests error response when no transactions are provided.

	@Test
	void testCreateCustomerNoTransaction() throws Exception {
		customerDto.setTransaction(List.of());

		mockMvc.perform(post("/api/rewards/addCustomer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Transaction list cannot be empty")));
	}

	// Tests error response when request body is null.

	@Test
	void testCreateCustomerNullBody() throws Exception {
		mockMvc.perform(post("/api/rewards/addCustomer").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Customer data is missing")));
	}

	// Tests reward calculation with valid date range for an existing customer.

	@Test
	void testCalculateValidRewards() throws Exception {
		MvcResult result = mockMvc
				.perform(post("/api/rewards/addCustomer").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(customerDto)))
				.andExpect(status().isCreated()).andReturn();

		Customer savedCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);

		mockMvc.perform(get("/api/rewards/calculateRewards/" + savedCustomer.getCustomerId())
				.param("startDate", "2024-01-01").param("endDate", "2024-12-31")).andExpect(status().isOk())
				.andExpect(jsonPath("$['Total Rewards']").exists());
	}

	// Tests error when start date is after end date in reward calculation.

	@Test
	void testCalculateRewardsInvalidDateOrder() throws Exception {
		mockMvc.perform(
				get("/api/rewards/calculateRewards/1").param("startDate", "2024-12-31").param("endDate", "2024-01-01"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Start date cannot be after end date.")));
	}

	// Tests error when customer ID is invalid (zero or negative).

	@Test
	void testCalculateRewardsMissingCustomerId() throws Exception {
		mockMvc.perform(
				get("/api/rewards/calculateRewards/0").param("startDate", "2024-01-01").param("endDate", "2024-12-31"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Customer ID must be a positive number")));
	}
}
