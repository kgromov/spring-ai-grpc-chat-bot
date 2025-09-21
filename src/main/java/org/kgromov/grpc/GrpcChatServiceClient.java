package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatMessage;
import com.kgromov.chat.bot.ChatResponse;
import com.kgromov.chat.bot.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Import(GrpcClientConfig.class)
@RequiredArgsConstructor
public class GrpcChatServiceClient {
    private final ChatServiceGrpc.ChatServiceStub chatAsyncStub;

    public String sendMessage(String message, Consumer<ChatResponse> onServerResponse) {
        var responseObserver = ResponseObserver.create(onServerResponse);
        chatAsyncStub.sendMessage(ChatMessage.newBuilder().setMessage(message).build(), responseObserver);
        return responseObserver.getList().getFirst().getMessage();
    }

    public void streamMessage(
            ChatMessage chatMessage,
            Runnable clientRequest,
            Consumer<ChatResponse> onServerResponse
    ) {
        var responseObserver = ResponseObserver.create(onServerResponse);
        StreamObserver<ChatMessage> requestStream = chatAsyncStub.streamMessages(responseObserver);
        var requestObserver = StreamObserverDecorator.create(requestStream, clientRequest);
        requestObserver.onNext(chatMessage);
    }
}
