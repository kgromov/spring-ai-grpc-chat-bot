package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatMessage;
import com.kgromov.chat.bot.ChatResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.kgromov.assistant.ChatService;

@RequiredArgsConstructor
public class ChatMessageHandler implements StreamObserver<ChatMessage> {
    private final StreamObserver<ChatResponse> responseObserver;
    private final ChatService chatService;

    @Override
    public void onNext(ChatMessage chatMessage) {
        String answer = chatService.answer(chatMessage.getMessage());
        ChatResponse response = ChatResponse.newBuilder().setMessage(answer).build();
        this.responseObserver.onNext(response);
    }

    @Override
    public void onError(Throwable throwable) {
        this.responseObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
        this.responseObserver.onCompleted();
    }
}
