package com.example;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaProducer {

    Logger log = Logger.getLogger(KafkaProducer.class);

    @Inject
    @Channel("events-out")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    Emitter<CamEventRequest> emitter;

    public CompletionStage<Void> produce(CamEventRequest payload) {
        log.debugf("producing kafka message: %s " + payload);
        return emitter.send(payload);
    }
    }
}
