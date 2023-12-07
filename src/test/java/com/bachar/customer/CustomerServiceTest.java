package com.bachar.customer;

import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        Assertions.assertThat(actual).isEqualTo(customer);
    }

    @Test
    void whenGetCustomerNotFoundThrowException() {
        //Given
        Long id = 1L;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //Then
        Assertions.assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id %d is not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        CustomerRegisterRequest request = new CustomerRegisterRequest("Bachar Daowd", "bachar@example.com", 45);
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(false);
        //When
        underTest.addCustomer(request);
//        Customer customer = Customer.builder().name(request.name())
//                .email(request.email()).age(request.age()).build();
        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        //Then
        verify(customerDao).insertCustomer(c.capture());

        Customer value = c.getValue();
        Assertions.assertThat(value.getId()).isEqualTo(null);
        Assertions.assertThat(value.getName()).isEqualTo("Bachar Daowd");
        Assertions.assertThat(value.getEmail()).isEqualTo("bachar@example.com");
        Assertions.assertThat(value.getAge()).isEqualTo(45);
    }

    @Test
    void addCustomerFailedEmailAlreadyExist() {
        //Given
        String email = "bachar@example.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegisterRequest request = new CustomerRegisterRequest("Bachar Daowd", "bachar@example.com", 45);
        //Then
        Assertions.assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
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