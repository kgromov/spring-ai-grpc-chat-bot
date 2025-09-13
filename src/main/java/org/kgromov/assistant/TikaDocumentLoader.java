package org.kgromov.assistant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Primary
@Component
@Slf4j
public class TikaDocumentLoader implements DocumentLoader {
    @SneakyThrows
    @Override
    public List<Document> loadDocument(Path filePath) {
        Resource resource = new ByteArrayResource(Files.readAllBytes(filePath));
//            Resource resource = new FileSystemResource(filePath.toFile());
        return this.loadDocument(resource);
    }

    @Override
    public List<Document> loadDocument(Resource resource) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("%s: loadDocument".formatted(this.getClass().getSimpleName()));
        try {
            log.debug("Loading document from resource = {}", resource.getFilename());
            var documentReader = new TikaDocumentReader(resource);
            var textSplitter = new TokenTextSplitter();
            return textSplitter.apply(documentReader.get());
//            Files.createFile(Paths.get(vectorStoreFile.getAbsolutePath()));
//            store.save(vectorStoreFile);
        } finally {
            stopWatch.stop();
            var taskInfo = stopWatch.lastTaskInfo();
            log.info("Time to {} = {} ms", taskInfo.getTaskName(), taskInfo.getTimeMillis());
        }
    }
}
