package com.bachar.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);

    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existPersonWithId(Long customerId);

    void deleteCustomer(Long customerId);
    void updateCustomer(Customer update);
}
