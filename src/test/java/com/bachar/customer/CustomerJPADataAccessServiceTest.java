package com.bachar.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        Long id = 1L;
        //When
        underTest.selectCustomerById(id);
        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer = new Customer(
                Faker.instance().name().fullName(),
                Faker.instance().internet().emailAddress(),
                45
        );
        //When
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        Customer customer = new Customer(
                Faker.instance().name().fullName(),
                Faker.instance().internet().emailAddress(),
                45
        );
        //When
        underTest.existsPersonWithEmail(customer.getEmail());
        //Then
        verify(customerRepository).existsCustomerByEmail(customer.getEmail());
    }

    @Test
    void existPersonWithId() {
        //Given
        Long id = 1L;
        //When
        underTest.existPersonWithId(id);
        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomer() {
        //Given
        Long id = 1L;
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = new Customer(
                Faker.instance().name().fullName(),
                Faker.instance().internet().emailAddress(),
                45
        );
        //When
        underTest.updateCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }
}