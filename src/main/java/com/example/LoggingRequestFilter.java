package com.example;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class LoggingRequestFilter  implements ContainerRequestFilter {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    private static final Logger LOG = Logger.getLogger(LoggingRequestFilter.class);
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String method = requestContext.getMethod();
        final String path = info.getPath();
        final String address = request.remoteAddress().toString();

        InputStream entityStream = requestContext.getEntityStream();
        String payload = new String(entityStream.readAllBytes());

        // Resetting the entity stream so it can be read again
        requestContext.setEntityStream(new ByteArrayInputStream(payload.getBytes()));

        LOG.infof("Request %s %s from IP %s. payload: %s ", method, path, address, payload);
    }
}
