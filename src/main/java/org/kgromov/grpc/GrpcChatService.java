package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatMessage;
import com.kgromov.chat.bot.ChatResponse;
import com.kgromov.chat.bot.ChatServiceGrpc.ChatServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.kgromov.assistant.ChatService;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Objects;

@GrpcService
@RequiredArgsConstructor
public class GrpcChatService extends ChatServiceImplBase {
    private final ChatService chatService;

    @Override
    public void sendMessage(ChatMessage request, StreamObserver<ChatResponse> responseObserver) {
        if (Objects.equals(request.getMessage(), "/exit")) {
            responseObserver.onNext( ChatResponse.newBuilder().setMessage("Bye!").build());
            responseObserver.onCompleted();
            return;
        }
        String answer = chatService.answer(request.getMessage());
        ChatResponse chatResponse = ChatResponse.newBuilder().setMessage(answer).build();
        responseObserver.onNext(chatResponse);
    }

    @Override
    public StreamObserver<ChatMessage> streamMessages(StreamObserver<ChatResponse> responseObserver) {
        return new ChatMessageHandler(responseObserver, chatService);
    }

}
