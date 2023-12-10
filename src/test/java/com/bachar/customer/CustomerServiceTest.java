package com.bachar.customer;

import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.RequestValidationException;
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
import static org.mockito.Mockito.*;

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
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        Long id = 1L;
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void deleteCustomerNotPossible() {
        //Given
        Long id = 1L;
        when(customerDao.existPersonWithId(id)).thenReturn(false);
        //When
        //Then
        Assertions.assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id %d is not found".formatted(id));
        verify(customerDao, never()).deleteCustomer(id);
    }

    @Test
    void updateAllCustomerProperties() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .name("Samer Daowd").email("samer@example.com").age(42).build();
        Mockito.when(customerDao.existsPersonWithEmail(updateRequest.email())).thenReturn(false);
        //When
        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //Then
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer updatedCustomer = customerArgumentCaptor.getValue();
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(updateRequest.name());
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(updateRequest.email());
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(updateRequest.age());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateOnlyCustomerName() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .name("Samer Daowd").build();
        //When
        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //Then
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer updatedCustomer = customerArgumentCaptor.getValue();
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(updateRequest.name());
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateOnlyCustomerEmail() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "samer@example.com";
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .email(newEmail).build();
        //When
        underTest.updateCustomer(id, updateRequest);
//        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //Then
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer updatedCustomer = customerArgumentCaptor.getValue();
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(updateRequest.email());
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateOnlyCustomerAge() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        int updatedAge = 42;
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .age(updatedAge).build();
        //When
        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //Then
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer updatedCustomer = customerArgumentCaptor.getValue();
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(updateRequest.age());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void CanNotUpdateCustomerEmail() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "samer@example.com";
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .email(newEmail).build();
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("Email already taken");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void CanNotUpdateCustomerWhenNoFieldProvidedToUpdate() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "Bachar Daowd", "bachar@example.com", 45);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder().build();
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest)).isInstanceOf(RequestValidationException.class).hasMessage("no data changes found");
        verify(customerDao, never()).updateCustomer(any());
    }


}