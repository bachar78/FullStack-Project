package com.bachar.customer;

public record CustomerRegisterRequest(String name,
                                      String email,
                                      Integer age) {
}
