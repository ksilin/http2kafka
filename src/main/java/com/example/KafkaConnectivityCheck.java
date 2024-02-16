package com.example;
import io.micrometer.core.instrument.Metrics;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class KafkaConnectivityCheck {

    @Inject
    AdminClient kafkaAdminClient;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final int checkPeriodSeconds = 10;

    Logger log = Logger.getLogger(KafkaConnectivityCheck.class);

    private final AtomicInteger connectedSinceGauge = new AtomicInteger(0);

    private volatile boolean isKafkaConnected = true;

    private String lastErrorMessage = "";


    private void startHealthCheck(@Observes StartupEvent ev) {
        scheduler.scheduleAtFixedRate(this::getClusterId, 0, checkPeriodSeconds, TimeUnit.SECONDS);
        Metrics.globalRegistry.gauge("connected-since-seconds", Collections.emptyList(), connectedSinceGauge);
    }


    private void getClusterId() {
        try {
            String clusterID = kafkaAdminClient.describeCluster(new DescribeClusterOptions().timeoutMs(1000)).clusterId().get();
            isKafkaConnected = true;
            log.debugv("connected to Kafka cluster: {0}", clusterID);
            connectedSinceGauge.getAndAdd(checkPeriodSeconds);
        } catch (Exception e) {
            log.warnv("unable to connect to Kafka cluster: {0}", e.getMessage());
            lastErrorMessage = e.getMessage();
            isKafkaConnected = false;
            connectedSinceGauge.set(0);
        }
    }

    public boolean isKafkaConnected() {
        return isKafkaConnected;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

}
