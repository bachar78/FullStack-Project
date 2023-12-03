package com.bachar.customer;

import com.bachar.AbstractTestcontainer;
import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        //Then
        assertThat(customers).isNotEmpty();
        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    void selectCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "_" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        String customerEmail = customer.getEmail();
        Long id = underTest.selectAllCustomers()
                .stream().filter(customer1 -> customer1.getEmail().equals(customerEmail))
                .map(customer1 -> customer1.getId())
                .findFirst().orElseThrow();

        //When
        Customer actual = underTest.selectCustomerById(id).orElseThrow();

        //Then
        assertThat(actual).isNotNull().satisfies(c -> {
            c.getId().equals(id);
            c.getEmail().equals(customerEmail);
            c.getName().equals(customer.getName());
            c.getAge().equals(customer.getAge());
        });
    }

    @Test
    void insertCustomer() {
        //Given
        Customer newCustomer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        //When
        underTest.insertCustomer(newCustomer);

        //Then
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        //When
        underTest.insertCustomer(customer);
        boolean result = underTest.existsPersonWithEmail(customer.getEmail());
        //Then
        assertThat(result).isTrue();
    }

    @Test
    void existPersonWithId() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        //When
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(customer1 -> customer1.getEmail().equals(customer.getEmail()))
                .map(customer1 -> customer1.getId()).findFirst().orElseThrow();
        //Then
        assertThat(underTest.existPersonWithId(id)).isTrue();
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