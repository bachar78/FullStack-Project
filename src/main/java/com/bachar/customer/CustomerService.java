package com.bachar.customer;


import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer customerId) {
        return customerDao.selectCustomerById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer with id %d is not found".formatted(customerId)));
    }

    public void addCustomer(CustomerRegisterRequest customerRegisterRequest) {
        String email = customerRegisterRequest.email();
        if (customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("Email already taken");
        }

        Customer customer = new Customer(customerRegisterRequest.name(),
                customerRegisterRequest.email(),
                customerRegisterRequest.age());
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer customerId) {
        if(!customerDao.existPersonWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with id %d is not found".formatted(customerId));
        }
        customerDao.deleteCustomer(customerId);
    }
}
