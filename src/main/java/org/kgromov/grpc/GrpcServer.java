package org.kgromov.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.autoconfigure.server.GrpcServerProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class GrpcServer {

    private final Server server;

    public GrpcServer(GrpcServerProperties serverProperties, BindableService... services) {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(serverProperties.getPort());
        List.of(services).forEach(serverBuilder::addService);
        this.server = serverBuilder.build();
    }

    @PostConstruct
    public void start() throws IOException {
        this.server.start();
        log.info("gRPC server started on port: {}", server.getPort());
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            log.info("Shutting down gRPC server");
            server.shutdown();
            log.info("gRPC server shut down");
        }
    }
}