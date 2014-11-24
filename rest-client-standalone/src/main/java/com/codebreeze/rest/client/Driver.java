package com.codebreeze.rest.client;

public class Driver {
    public static final void main(final String...args){
        String result = restTemplate.getForObject("http://example.com/hotels/{hotel}/bookings/{booking}", String.class, "42", "21");

    }
}
