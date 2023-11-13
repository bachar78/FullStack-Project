package com.bachar.customer;

public record CustomerUpdateRequest (String name,
                                     String email,
                                     Integer age) {
}
