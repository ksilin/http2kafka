package com.example;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.stream.Stream;

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

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    void onStart(@Observes StartupEvent ev) {
        Stream.of("Hello", "with", "SmallRye", "reactive", "message").forEach(string -> emitter.send(string));
    }
}
