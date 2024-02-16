package com.example;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/events")
@RolesAllowed("admin")
public class EventsResource {


    Logger log = Logger.getLogger(EventsResource.class);


    @Inject
    KafkaProducer processor;

    @Inject
    KafkaConnectivityCheck connectivityCheck;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEvent(CamEventRequest event) {
        if(!connectivityCheck.isKafkaConnected()) return Response.status(503, connectivityCheck.getLastErrorMessage()).build();

        processor.produce(event);
        return Response.accepted().build();
    }
}
