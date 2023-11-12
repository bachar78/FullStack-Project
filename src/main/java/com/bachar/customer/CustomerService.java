package com.bachar.customer;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    private final CustomerDao customerDao;


    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer customerId) {
        return customerDao.selectCustomerById(customerId).orElseThrow(()-> new IllegalArgumentException("Customer not found"));
    }
}
