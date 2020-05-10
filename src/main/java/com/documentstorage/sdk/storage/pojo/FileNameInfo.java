package com.documentstorage.sdk.storage.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileNameInfo {

    private String fileName;

    private String fileExtension;
}
