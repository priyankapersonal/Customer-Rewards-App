package com.infy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing a transaction made by a customer.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	private double amount;

	private LocalDate date;

	@ManyToOne
	@JoinColumn(name = "customerId")
	@JsonIgnore
	private Customer customer;

	@Override
	public String toString() {
		return "Transaction{id=" + transactionId + ", amount=" + amount + ", date=" + date + "}";
	}
}