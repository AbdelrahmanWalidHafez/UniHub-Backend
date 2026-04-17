package com.unihub.ai.service.impl;

import com.unihub.ai.service.IVectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorStoreServiceImpl implements IVectorStoreService {

    private final VectorStore vectorStore;

    @Async
    @Override
    public void loadDoc(Resource doc, Map<String,Object> metaData) {
        try {
            List<Document> docs = fetchDocs(doc);
            List<Document> textSplitterDocs = splitDocs(docs);
            for (Document chunk : textSplitterDocs) {
                chunk.getMetadata().putAll(metaData);
            }
            vectorStore.add(textSplitterDocs);
        }catch (Exception e){
            log.error("Error while loading document: {}",e.getMessage());
        }
    }

    private List<Document> fetchDocs(Resource doc) {
        return new TikaDocumentReader(doc).get();
    }

    private List<Document> splitDocs(List<Document> docs) {
        return TokenTextSplitter.builder()
                .withChunkSize(430)
                .withMaxNumChunks(300)
                .build()
                .split(docs);
    }



}
