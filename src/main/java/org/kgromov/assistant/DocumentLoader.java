package org.kgromov.assistant;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;


public interface DocumentLoader {

    List<Document> loadDocument(Path filePath);

    List<Document> loadDocument(Resource resource);
}
