package com.example;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaProducer {

    @Inject
    @Channel("events-out")
    Emitter<String> emitter;

    public void produce(String payload) {
        // Process the payload
        System.out.println("Received payload: " + payload);
        emitter.send(payload);
    }
}
