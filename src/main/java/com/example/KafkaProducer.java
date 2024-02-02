package com.example;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaProducer {

    Logger log = Logger.getLogger(KafkaProducer.class);

    @Inject
    @Channel("events-out")
    Emitter<CamEventRequest> emitter;

    public void produce(CamEventRequest payload) {
        log.debugf("Received payload: %s " + payload);
        emitter.send(payload);
    }
}
