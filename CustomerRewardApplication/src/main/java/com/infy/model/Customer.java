package com.infy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a customer in the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	private String customerName;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Transaction> transaction;

	@Override
	public String toString() {
		return "Customer{id=" + customerId + ", name=" + customerName + "}";
	}
}