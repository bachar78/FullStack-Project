package com.bachar.customer;

import com.bachar.AbstractTestcontainer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainer {

    @Autowired
    private CustomerRepository underTest;
    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        //When
        underTest.save(customer);
        var result = underTest.existsCustomerByEmail(customer.getEmail());
        //Then
        assertThat(result).isTrue();
    }

    @Test
    void existsCustomerBydEmailFailsWithNonExistingEmail() {
        //Given
        var email = FAKER.internet().emailAddress();

        var result = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(result).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + UUID.randomUUID(),
                45
        );
        //When
        underTest.save(customer);
        Long id = underTest.findAll().stream().filter(customer1 -> customer1.getEmail().equals(customer.getEmail()))
                .map(customer1 -> customer1.getId()).findFirst().orElseThrow();
        //Then
        assertThat(underTest.existsCustomerById(id)).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWithNonExistingId() {
        //Given
        var id = Faker.instance().random().nextLong();
        //Then
        assertThat(underTest.existsCustomerById(id)).isFalse();
    }
}