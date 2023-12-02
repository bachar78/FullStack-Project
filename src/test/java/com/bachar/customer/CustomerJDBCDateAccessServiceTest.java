package com.bachar.customer;

import com.bachar.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.UUID;


class CustomerJDBCDateAccessServiceTest extends AbstractTestcontainer {
    private CustomerJDBCDateAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDateAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    @DisplayName("Select all customers")
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "_" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //When
        List<Customer> customers = underTest.selectAllCustomers();
        System.out.println("the customer size is: " + customers.size());
        System.out.println(customer.toString());
        //Then
        assertThat(customers.size() != 0).isTrue();
    }

    @Test
    void selectCustomerById() {
        //Given

        //When

        //Then
    }

    @Test
    void insertCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void existsPersonWithEmail() {
        //Given

        //When

        //Then
    }

    @Test
    void existPersonWithId() {
        //Given

        //When

        //Then
    }

    @Test
    void deleteCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void updateCustomer() {
        //Given

        //When

        //Then
    }
}