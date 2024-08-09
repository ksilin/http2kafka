package com.example;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.eclipse.microprofile.faulttolerance.Retry;
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

    @Retry(maxRetries = 5, delay = 1000, maxDuration = 5000, jitter = 500)
    //@Backoff(delay = 200, maxDelay = 2000, multiplier = 1.5)
    public CompletionStage<Void> produce(CamEventRequest payload) {
        log.debugf("producing kafka message:" + payload);
        return emitter.send(payload);
    }

    public void produceSync(CamEventRequest payload) {
        produce(payload).toCompletableFuture().join();
    }


    public void produceErr(CamEventRequest payload) {
        produce(payload).thenAccept(result ->
                                            log.info("Message sent successfully"))
                .exceptionally(e -> {
                    log.error("Failed to send message", e);
                    return null;
                });
    }

    public Future<Either<Throwable, String>> produceVavr(CamEventRequest payload) {
        return Future.fromCompletableFuture(produce(payload).toCompletableFuture())
                .map(value -> Either.<Throwable, String>right("Message sent successfully"))
                .recover(e -> Either.left(e));
    }

}
