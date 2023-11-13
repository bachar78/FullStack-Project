package com.bachar.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);

    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existPersonWithId(Integer customerId);

    void deleteCustomer(Integer customerId);
    void updateCustomer(Customer update);
}
