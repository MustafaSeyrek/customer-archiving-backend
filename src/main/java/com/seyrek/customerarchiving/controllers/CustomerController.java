package com.seyrek.customerarchiving.controllers;

import com.seyrek.customerarchiving.entities.Customer;
import com.seyrek.customerarchiving.responses.CustomerResponse;
import com.seyrek.customerarchiving.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.getCustomerById(id), OK);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomerById(@PathVariable Long id, @RequestBody Customer newCustomer) {
        Customer customer = customerService.updateCustomerById(id, newCustomer);
        if (customer != null) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>(OK);
    }
}
