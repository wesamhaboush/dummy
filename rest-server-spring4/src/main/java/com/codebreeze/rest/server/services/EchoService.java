package com.codebreeze.rest.server.services;

import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class EchoService {

    public String echo(final String request) {
        return new Date() + ":" + request;
    }
}
