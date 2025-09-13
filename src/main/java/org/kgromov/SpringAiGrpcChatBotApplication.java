package org.kgromov;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.grpc.autoconfigure.client.GrpcClientProperties;
import org.springframework.grpc.autoconfigure.server.GrpcServerProperties;

@EnableConfigurationProperties({GrpcServerProperties.class, GrpcClientProperties.class})
@Push
@SpringBootApplication
public class SpringAiGrpcChatBotApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiGrpcChatBotApplication.class, args);
    }

}
