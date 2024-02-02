package com.example;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/events")
@RolesAllowed("admin")
public class EventsResource {

    @Inject
    KafkaProducer processor;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEvent(CamEventRequest event) {
        processor.produce(event);
        return Response.accepted().build();
    }
}
