package com.example;

import io.quarkus.security.spi.runtime.AuthenticationFailureEvent;
import io.quarkus.security.spi.runtime.AuthenticationSuccessEvent;
import io.quarkus.security.spi.runtime.AuthorizationFailureEvent;
import io.quarkus.security.spi.runtime.AuthorizationSuccessEvent;
import io.quarkus.security.spi.runtime.SecurityEvent;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import org.jboss.logging.Logger;

public class SecurityEventObserver {

    private static final Logger LOG = Logger.getLogger(SecurityEventObserver.class.getName());

    void observeAuthenticationSuccess(@ObservesAsync AuthenticationSuccessEvent event) {
        LOG.warnf("User '%s' has authenticated successfully", event.getSecurityIdentity().getPrincipal().getName());
        event.getEventProperties().forEach((k, v) -> LOG.warnf("%s : %s", k, getDetailedInfo(v)));
    }

    void observeAuthenticationFailure(@ObservesAsync AuthenticationFailureEvent event) {
        RoutingContext routingContext = (RoutingContext) event.getEventProperties().get(RoutingContext.class.getName());
        LOG.warnf("Authentication failed, request path: '%s'", routingContext.request().path());
        event.getEventProperties().forEach((k, v) -> LOG.warnf("%s : %s", k, getDetailedInfo(v)));

    }

    void observeAuthorizationSuccess(@ObservesAsync AuthorizationSuccessEvent event) {
        String principalName = getPrincipalName(event);
        if (principalName != null) {
            LOG.warnf("User '%s' has been authorized successfully", principalName);
            event.getEventProperties().forEach((k, v) -> LOG.warnf("%s : %s", k, getDetailedInfo(v)));
        }
    }

    void observeAuthorizationFailure(@Observes AuthorizationFailureEvent event) {
        LOG.warnf(event.getAuthorizationFailure(), "User '%s' authorization failed", event.getSecurityIdentity().getPrincipal().getName());
        event.getEventProperties().forEach((k, v) -> LOG.warnf("%s : %s", k, getDetailedInfo(v)));
    }

    private static String getPrincipalName(SecurityEvent event) {
        if (event.getSecurityIdentity() != null) {
            return event.getSecurityIdentity().getPrincipal().getName();
        }
        return null;
    }

    private static String getDetailedInfo(Object value) {
        if (value instanceof RoutingContext) {
            RoutingContext context = (RoutingContext) value;
            return String.format("RoutingContext{path=%s, method=%s, headers=%s}",
                                 context.request().path(),
                                 context.request().method(),
                                 context.request().headers());
        }
        return value.toString();
    }

}