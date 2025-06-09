package com.infy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infy.model.Customer;

/**
 * Repository interface for managing Customer entities. Extends JpaRepository to
 * provide CRUD operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
