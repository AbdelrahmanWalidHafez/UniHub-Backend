package com.unihub.ai.service;

import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.UUID;

public interface IVectorStoreService {

    void loadDoc(Resource doc,Map<String,Object>metadata);

    void deleteDoc(UUID materialId);
}
