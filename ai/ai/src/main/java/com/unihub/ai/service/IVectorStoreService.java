package com.unihub.ai.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.UUID;

public interface IVectorStoreService {

    void loadDoc(ByteArrayResource doc, Map<String,Object>metadata);

    void deleteDoc(UUID materialId);
}
