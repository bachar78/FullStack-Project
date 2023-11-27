package com.bachar;

import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Application is running");
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            List<Customer> customers = List.of(Customer.builder().name("Alex").email("alex@gmail.com").age(21).build(),
                    Customer.builder().name("Jamila").email("jamila@gmail.com").age(19).build());
            customerRepository.saveAll(customers);
        };
    }

}
