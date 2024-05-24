package com.example;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MyReactiveMessagingApplicationTest {

    @Inject
    KafkaCamEventProducer application;

    @Test
    void test() {
      //  assertEquals("HELLO", application.toUpperCase(Message.of("Hello")).getPayload());
      //  assertEquals("BONJOUR", application.toUpperCase(Message.of("bonjour")).getPayload());
    }
}
