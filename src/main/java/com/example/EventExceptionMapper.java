package com.example;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Provider
public class EventExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    ContainerRequestContext request;

    @Override
    @ServerExceptionMapper
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();

        String errorMessage = "Request failed";
        if (status == 400) {
            errorMessage = "Malformed request payload";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getResponse().getStatus(),
                errorMessage,
                getUri(request)
        );

        return Response
                .status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public record ErrorResponse(int status, String error, String uri) {
    }

    private String getUri(ContainerRequestContext request) {
        return request.getUriInfo().getAbsolutePath().toString();
    }

}
