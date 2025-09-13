package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public ChatServiceGrpc.ChatServiceBlockingStub chatBlockingStub(ManagedChannel channel) {
        return ChatServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public ChatServiceGrpc.ChatServiceStub chatAsyncStub(ManagedChannel channel) {
        return ChatServiceGrpc.newStub(channel);
    }

   /* @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter(){
        return new ProtobufJsonFormatHttpMessageConverter(
                JsonFormat.parser().ignoringUnknownFields(),
                JsonFormat.printer().omittingInsignificantWhitespace().includingDefaultValueFields()
        );
    }*/

}
