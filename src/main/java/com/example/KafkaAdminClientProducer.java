package com.example;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

@Singleton
public class KafkaAdminClientProducer {
    @Produces
    @Singleton
    @Unremovable
    // kafka.ssl.endpoint.identification.algorithm=https

    public AdminClient produceAdminClient(@ConfigProperty(name = "kafka.bootstrap.servers") String bootstrapServers,
                                          @ConfigProperty(name = "kafka.security.protocol", defaultValue = "PLAINTEXT") String securityProtocol,
                                          @ConfigProperty(name = "kafka.sasl.mechanism", defaultValue = "UNUSED") String saslMechanism,
                                          @ConfigProperty(name = "kafka.sasl.jaas.config", defaultValue = "UNUSED") String jaasConfig) {
        return createAdminClient(bootstrapServers, securityProtocol, saslMechanism, jaasConfig);
    }

    public AdminClient createAdminClient( String bootstrapServers,
                                           String securityProtocol,
                                          String saslMechanism,
                                          String jaasConfig) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        //TODO - add host verification HTTPS
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return AdminClient.create(props);
    }
}
