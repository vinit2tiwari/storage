package com.documentstorage.sdk.storage.impl;

import com.documentstorage.sdk.storage.exceptions.DocumentStoreException;
import com.documentstorage.sdk.storage.interfaces.StorageEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageEngineFactory {

    @Autowired
    private List<StorageEngine> storageEngines;

    private Map<String , StorageEngine> storageEngineMap = new HashMap<>();

    @PostConstruct
    public void initEngineFactory(){
        for(StorageEngine storageEngine : storageEngines){
            storageEngineMap.put(storageEngine.getStorageEngineName() , storageEngine);
        }
    }

    public StorageEngine getInstance(String storageEngineName) throws DocumentStoreException {
        if(storageEngineMap.containsKey(storageEngineName)){
            return storageEngineMap.get(storageEngineName);
        }

        throw new DocumentStoreException("Invalid engine store name passed :: " + storageEngineName);
    }

}
