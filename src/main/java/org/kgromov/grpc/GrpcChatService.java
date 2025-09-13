package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatMessage;
import com.kgromov.chat.bot.ChatResponse;
import com.kgromov.chat.bot.ChatServiceGrpc.ChatServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.kgromov.assistant.ChatService;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcChatService extends ChatServiceImplBase {
    private final ChatService chatService;

    @Override
    public void sendMessage(ChatMessage request, StreamObserver<ChatResponse> responseObserver) {
        String answer = chatService.answer(request.getMessage());
        ChatResponse chatResponse = ChatResponse.newBuilder().setMessage(answer).build();
        responseObserver.onNext(chatResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void streamMessages(ChatMessage request, StreamObserver<ChatResponse> responseObserver) {
        chatService.answerStreaming(request.getMessage())
                .map(response -> response.getResult().getOutput().getText())
                .map(message -> ChatResponse.newBuilder().setMessage(message).build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
//                .subscribe(
//                        (message) -> responseObserver.onNext(message),
//                        (error) -> responseObserver.onError(error),
//                        () -> responseObserver.onCompleted()
//                );
    }

}
