package com.documentstorage.sdk.storage.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.documentstorage.sdk.storage.exceptions.DocumentStoreException;
import com.documentstorage.sdk.storage.interfaces.StorageEngine;
import com.documentstorage.sdk.storage.pojo.DocumentMetadata;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalStorageEngineTest {

    @Value("${storage.name}")
    private String engineName;

    private StorageEngine storageEngine;

    @Autowired
    private StorageEngineFactory storageEngineFactory;

    @PostConstruct
    public void init() throws DocumentStoreException {
        storageEngine = storageEngineFactory.getInstance(engineName);
    }

    @Test
    @SneakyThrows
    public void saveDocumentTest(){
        DocumentMetadata documentMetadata = storageEngine.saveDocument(System.getProperty("user.dir") + "/src/test/resources/testFile.txt");
        assertNotNull(documentMetadata);
        assertNotNull(documentMetadata.getDocumentId());
        System.out.println(documentMetadata.getDocumentId());
    }

    @Test
    @SneakyThrows
    public void getDocumentInfoTest(){
        DocumentMetadata documentMetadata = storageEngine.saveDocument(System.getProperty("user.dir") + "/src/test/resources/testFile.txt");
        DocumentMetadata updatedMetadata = storageEngine.getDocumentInfo(documentMetadata.getDocumentId());
        assertNotNull(updatedMetadata);
        assertEquals(updatedMetadata.getCreationTime() , documentMetadata.getCreationTime());
        assertEquals(updatedMetadata.getLastModifiedTime() , documentMetadata.getCreationTime());
        assertEquals(updatedMetadata.getDocumentName() , documentMetadata.getDocumentName());
        assertEquals(updatedMetadata.getVersion() , documentMetadata.getVersion());
    }

    @Test
    @SneakyThrows
    public void renameFileTest(){
        DocumentMetadata documentMetadata = storageEngine.saveDocument(System.getProperty("user.dir") + "/src/test/resources/testFile.txt");
        DocumentMetadata renmaedMetadata = storageEngine.renameDocument(documentMetadata.getDocumentId() , "testFileNew");
        assertNotNull(documentMetadata);
        assertEquals(renmaedMetadata.getDocumentName() , "testFileNew");
        assertEquals(renmaedMetadata.getVersion() ,documentMetadata.getVersion()+1);
        assertEquals(renmaedMetadata.getCreationTime() , documentMetadata.getCreationTime());
    }

    @Test
    @SneakyThrows
    public void deleteFileTest(){
        DocumentMetadata documentMetadata = storageEngine.saveDocument(System.getProperty("user.dir") + "/src/test/resources/testFile.txt");
        boolean result = storageEngine.deleteDocument(documentMetadata.getDocumentId());
        assert (result == true);
    }
}
