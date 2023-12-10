package com.bachar;

import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Application is running");
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        var faker = new Faker();
        Random random = new Random();
        return args -> {
            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();
            var customer = Customer.builder().name(firstName +" "+ lastName).email(firstName + "." + lastName + "@example.com").age(random.nextInt(16, 90)).build();
            customerRepository.save(customer);
        };
    }

//    @Bean
//    public String deleteAllCustomers(CustomerRepository customerRepository) {
//        customerRepository.deleteAll();
//        return "done";
//    }

}
