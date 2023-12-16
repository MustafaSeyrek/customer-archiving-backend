package com.seyrek.customerarchiving.repositories;

import com.seyrek.customerarchiving.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
