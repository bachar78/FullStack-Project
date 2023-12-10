package com.bachar.customer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerRowMapperTest {
    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper underTest = new CustomerRowMapper();
        //Given
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("Bachar Daowd");
        Mockito.when(resultSet.getString("email")).thenReturn("bachar@example.com");
        Mockito.when(resultSet.getInt("age")).thenReturn(45);

        Customer expectedCustomer = Customer.builder().id(1L).name("Bachar Daowd").email("bachar@example.com").age(45).build();
        //When

        Customer customer = underTest.mapRow(resultSet, 1);
        assertThat(customer).isInstanceOf(Customer.class);
//        assertThat(customer.getId()).isEqualTo(expectedCustomer.getId());
//        assertThat(customer.getName()).isEqualTo(expectedCustomer.getName());
//        assertThat(customer.getEmail()).isEqualTo(expectedCustomer.getEmail());
//        assertThat(customer.getAge()).isEqualTo(expectedCustomer.getAge());
        assertThat(customer).isEqualTo(expectedCustomer);
    }
}