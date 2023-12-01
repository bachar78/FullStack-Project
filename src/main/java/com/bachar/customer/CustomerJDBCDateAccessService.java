package com.bachar.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDateAccessService implements CustomerDao {

    final private JdbcTemplate jdbcTemplate;
    final private CustomerRowMapper customerRowMapper;

    public CustomerJDBCDateAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }


    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        return Optional.empty();
    }

    ;

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES(?,?,?)
                """;
        int result = jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge());
        System.out.println("insertCustomer result " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existPersonWithId(Integer customerId) {
        return false;
    }

    @Override
    public void deleteCustomer(Integer customerId) {

    }

    @Override
    public void updateCustomer(Customer update) {

    }
}