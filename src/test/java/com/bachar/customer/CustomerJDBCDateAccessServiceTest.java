package com.bachar.customer;

import com.bachar.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
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
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
                assertThat(c.getName()).isEqualTo(customer.getName());
                assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
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
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(newCustomer.getEmail()))
                .findFirst()
                .map(c -> c.getId()).orElseThrow();
        boolean existPersonByEmail = underTest.existsPersonWithEmail(newCustomer.getEmail());
        boolean existPersonById = underTest.existPersonWithId(id);

        //Then
        assertThat(existPersonByEmail).isTrue();
        assertThat(existPersonById).isTrue();
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
    void existPersonWithInvalidId() {
        //Given
        Long id = (long) -1;
        //Then
        assertThat(underTest.existPersonWithId(id)).isFalse();
    }

    @Test
    void deleteCustomer() {
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

        //When
        underTest.deleteCustomer(id);
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(customer1 -> customer1.getEmail().equals(customer.getEmail()))
                .map(customer1 -> customer1.getId()).findFirst().orElseThrow();
        customer.setName("Bachar Daowd");
        customer.setEmail("bachar.daowd@gmail.com");
        //When
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail()).isNotEqualTo(customer.getEmail());
            assertThat(c.getName()).isNotEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }
}