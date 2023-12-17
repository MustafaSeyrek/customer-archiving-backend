package com.seyrek.customerarchiving.services;

import com.seyrek.customerarchiving.entities.Customer;
import com.seyrek.customerarchiving.repositories.CustomerRepository;
import com.seyrek.customerarchiving.responses.CustomerResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll().stream().map(u -> new CustomerResponse(u.getId(), u.getFullName(), userService.getUserById(u.getCreatedId()).getUsername(), u.getCreatedDate()
        ,  u.getUpdatedId() == null ? "" : userService.getUserById(u.getUpdatedId()).getUsername(), u.getUpdatedDate())).toList();
    }

    public Customer createCustomer(Customer newCustomer) {
        newCustomer.setCreatedDate(new Date());
        return customerRepository.save(newCustomer);
    }

    public Customer updateCustomerById(Long id, Customer newCustomer) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer foundCustomer = customer.get();
            foundCustomer.setFullName(newCustomer.getFullName());
            foundCustomer.setUpdatedDate(new Date());
            foundCustomer.setUpdatedId(newCustomer.getUpdatedId());
            customerRepository.save(foundCustomer);
            return foundCustomer;
        } else {
            return null;
        }
    }

    public Customer getCustomerById(Long id) {
        if(id == null) return null;
        return customerRepository.findById(id).orElse(null);
    }

    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
