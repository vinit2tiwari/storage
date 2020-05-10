package com.documentstorage.sdk.storage.pojo;

import java.sql.Timestamp;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class DocumentMetadata {

    private String documentName;

    private String documentId;

    private Timestamp creationTime;

    private Timestamp lastModifiedTime;

    private Long size;

    private int version = 1;

}
