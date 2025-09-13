package org.kgromov.grpc;

import com.kgromov.chat.bot.ChatMessage;
import com.kgromov.chat.bot.ChatResponse;
import com.kgromov.chat.bot.ChatServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrpcChatServiceClient {

    private final ChatServiceGrpc.ChatServiceStub chatAsyncClient;

    public String sendMessage(String message){
        var responseObserver = ResponseObserver.<ChatResponse>create();
        chatAsyncClient.sendMessage(ChatMessage.newBuilder().setMessage(message).build(), responseObserver);
        responseObserver.await();
        return responseObserver.getList().getFirst().getMessage();
    }
}
