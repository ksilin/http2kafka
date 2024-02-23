package com.example;

import io.smallrye.reactive.messaging.health.HealthReport;
import io.smallrye.reactive.messaging.kafka.KafkaConnector;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;
@Path("/events")
@RolesAllowed("admin")
public class EventsResource {


    Logger log = Logger.getLogger(EventsResource.class);


    @Inject
    KafkaProducer processor;

    @Inject
    @Any
    KafkaConnector conn;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEvent(CamEventRequest event) {

        if (!conn.getReadiness().isOk())
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(conn.getReadiness().getChannels().stream()
                                    .map(channel -> String.format("channel %s ok: %s %s", channel.getChannel(), channel.isOk(), channel.getMessage()))
                                    .collect(Collectors.joining("\\n")))
                    .type(MediaType.TEXT_PLAIN).build();

        processor.produce(event);
        return Response.accepted().build();
    }
}
