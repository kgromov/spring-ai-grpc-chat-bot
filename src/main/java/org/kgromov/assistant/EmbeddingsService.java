package org.kgromov.assistant;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingsService {
    private final VectorStore vectorStore;
    private final DocumentLoader documentLoader;

    @SneakyThrows
    public void processDocument(Path filePath) {
        log.info("Start uploading to embeddings store...");
        var extension = FileNameUtils.getExtension(filePath);
        var documents = documentLoader.loadDocument(filePath);
        vectorStore.add(documents);
        log.info("Finished uploading to embeddings store");
    }
}
