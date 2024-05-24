package com.example;

import com.mifmif.common.regex.Generex;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@QuarkusTest
@QuarkusTestResource(value = KafkaCompanionResource.class, initArgs = {
        @ResourceArg(name = "kafka.port", value = "59092"), // Fixed port for kafka, by default it will be exposed on a random port
        @ResourceArg(name = "num.partitions", value = "3"), // Other custom broker configurations
})
public class KafkaCamEventProducerTest {

    Logger log = Logger.getLogger(KafkaCamEventProducerTest.class);

    @InjectKafkaCompanion
    KafkaCompanion companion;

    @Inject
    KafkaCamEventProducer producer;

    @ConfigProperty(name = "mp.messaging.outgoing.events-out.topic")
    String eventsTopicName;

    // TODO - create test topic
    @BeforeEach
    void beforeEach(){
        companion.topics().createAndWait(eventsTopicName, 3);
    }

    @Test
    public void testProduce(){

        // [com.exa.KafkaCamEventProducerTest] (Test worker) produced 20000 messages in 2597 ms
        var plateCount = 50000;
        var generexFormat = "[A-Z]{3} [A-Z]{2} \\d{4}";
        Generex generex = new Generex(generexFormat);

        List<String> plates = IntStream.range(0, plateCount).mapToObj(i -> generex.random()).toList();

        var nowMillisString = Long.toString(System.currentTimeMillis());

        // String timestampString, String carID, String plate, String sensorProviderID
        List<CamEventRequest> camEventRequests = plates.stream().map(plate -> MockDataFactory.makeCamEventRequest(nowMillisString, plate, plate, MockDataFactory.sensorProviderId_notEntryExit)).toList();

        var produceStart = System.currentTimeMillis();
        Stream<CompletableFuture<Void>> completionStageStream = camEventRequests.stream().map(r -> producer.produce(r).toCompletableFuture());
        CompletableFuture[] array = completionStageStream.toList().toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(array).join();
        var produceStop = System.currentTimeMillis();

        log.infof("produced %d messages in %d ms", plateCount, produceStop - produceStart);

    }



}
