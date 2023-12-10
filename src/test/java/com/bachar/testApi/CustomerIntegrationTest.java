package com.bachar.testApi;

import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRegisterRequest;
import com.bachar.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    public static final String API_V_1_CUSTOMERS = "api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;
    private Faker faker = new Faker();
    private Random random = new Random();

    @Test
    void canRegisterUser() {
        Name name = faker.name();
        String userName = name.fullName();
        String email = name.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = random.nextInt(1, 90);
        CustomerRegisterRequest request = new CustomerRegisterRequest(userName, email, age);

        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegisterRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get All customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(userName, email, age);
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        Long id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        Customer responseBody = webTestClient.get()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

        assertThat(expectedCustomer).isEqualTo(responseBody);

        webTestClient.delete()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        webTestClient.get().uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        Name name = faker.name();
        String userName = name.fullName();
        String email = name.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = random.nextInt(1, 90);
        CustomerRegisterRequest request = new CustomerRegisterRequest(userName, email, age);

        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegisterRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //Get All customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        CustomerUpdateRequest updatedCustomer = new CustomerUpdateRequest("Bachar", faker.internet().emailAddress(), 45);
        webTestClient.put().uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedCustomer), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();
        Customer expectecCustomer = new Customer(id, updatedCustomer.name(), updatedCustomer.email(), updatedCustomer.age());
        Customer responseBody = webTestClient.get().uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();
        assertThat(responseBody).isEqualTo(expectecCustomer);

    }
}
