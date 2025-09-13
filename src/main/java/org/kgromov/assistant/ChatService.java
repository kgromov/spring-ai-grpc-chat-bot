package org.kgromov.assistant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource promptResource;

    public String answer(String question) {
        return chatClient.prompt(this.prompt(question))
                .call()
                .content();
    }

    public Flux<ChatResponse> answerStreaming(String question) {
        return chatClient.prompt(this.prompt(question))
//                .user(userSpec -> userSpec.params(
//                        Map.of(
//                                "input", question,
//                                "documents", documentsContent
//                        )
//                ))
                .stream()
                .chatResponse();
    }

    public Prompt prompt(String question) {
        var searchRequest = SearchRequest.builder()
                .query(question)
                .similarityThreshold(0.5)
                .topK(4)
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        String documentsContent = documents.stream().map(Document::getText).collect(joining("\n"));

        var promptTemplate = new PromptTemplate(promptResource);
        return promptTemplate.create(
                Map.of(
                        "input", question,
                        "documents", documentsContent
                )
        );
    }
}
