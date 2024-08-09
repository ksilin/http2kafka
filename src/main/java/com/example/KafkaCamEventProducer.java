package com.example;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaCamEventProducer {

    @Inject
    @Channel("events-out")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    Emitter<CamEventRequest> emitter;

    Logger log = Logger.getLogger(KafkaCamEventProducer.class);

    public CompletionStage<Void> produce(CamEventRequest payload) {
        log.debugf("producing kafka message:" + payload);
        return emitter.send(payload);
    }

}
