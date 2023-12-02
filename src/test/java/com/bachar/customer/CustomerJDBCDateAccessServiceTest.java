package com.bachar.customer;

import com.bachar.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class CustomerJDBCDateAccessServiceTest extends AbstractTestcontainer {
    private CustomerJDBCDateAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDateAccessService(getJdbcTemplate(),customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given

        //When

        //Then
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