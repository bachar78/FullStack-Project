package com.bachar.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    // db
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        customers.add(Customer.builder().id(1).name("Alex").email("alex@gmail.com").age(21).build());
        customers.add(Customer.builder().id(2).name("Jamila").email("jamila@gmail.com").age(19).build());
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWithId(Integer customerId) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(customerId));
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customers.stream()
                .filter(c -> Objects.equals(c.getId(), customerId))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }


}
