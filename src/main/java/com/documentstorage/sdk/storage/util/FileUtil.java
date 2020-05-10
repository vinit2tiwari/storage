package com.documentstorage.sdk.storage.util;

import com.documentstorage.sdk.storage.exceptions.DocumentStoreException;
import com.documentstorage.sdk.storage.impl.LocalStorageEngine;
import com.documentstorage.sdk.storage.pojo.FileNameInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This method provides util class to help local file processing.
 */
@Component
@Slf4j
public class FileUtil {

    /**
     * This method returns an object having file name and extension
     * @param lFile : File name
     * @return FileNameInfo object having file name and extension
     */
    public FileNameInfo getFileName(String lFile){
        if(lFile == null || lFile.isEmpty()){
            return new FileNameInfo();
        }
        FileNameInfo lFileNameInfo = new FileNameInfo();

        int extIndex = lFile.lastIndexOf(".");
        if(extIndex > 0){
            String lFileName = lFile.substring(0 , extIndex);
            lFileNameInfo.setFileName(lFileName);
            lFileNameInfo.setFileExtension(lFile.substring(extIndex , lFile.length()));
        } else {
            lFileNameInfo.setFileExtension(".tmp");
            lFileNameInfo.setFileName("local-storage");
        }

        return lFileNameInfo;
    }

    /**
     * This methdo returns the file name with extension from absoulte file path
     * @param absolutefilePath : Absolute file path of the doucment
     * @return : File name
     */
    public  String getCanonicalName(String absolutefilePath){
        if(absolutefilePath == null || absolutefilePath.isEmpty()){
            return "";
        }

        int index = absolutefilePath.lastIndexOf("/");
        if(index > 0) {
            return absolutefilePath.substring(index + 1, absolutefilePath.length());
        }

        return "";
    }

    /**
     * This method creates a child folder with directoryName provided, in case of failure new directory is created and name returned.
     * @param directoryName : Name of the directory to be created
     * @return : Directory name with which directory was actually created
     * @throws DocumentStoreException
     */
    public String createChildDir(String directoryName) throws DocumentStoreException {
        File directory = new File(LocalStorageEngine.LOCAL_STORAGE_FOLDER +  directoryName);

        while(directory.exists()){
            directoryName = String.valueOf(System.currentTimeMillis() + Math.random());
            directory = new File(LocalStorageEngine.LOCAL_STORAGE_FOLDER + directoryName);
        }

        if(!directory.mkdir()){
            throw new DocumentStoreException("Error occured while storing file ");
        }
        return directoryName;
    }

    /**
     * This method returns the latest version of the document stored.
     * @param fileIdentifier
     * @return
     * @throws DocumentStoreException
     */
    public int getLatestVersion(String fileIdentifier) throws DocumentStoreException {
        try{
            File folder = new File(LocalStorageEngine.LOCAL_STORAGE_FOLDER+ fileIdentifier);
            if (!folder.exists()) {
                throw new DocumentStoreException("File not found :: " + fileIdentifier);
            }

            Optional<File> versionFolder = Arrays.stream(folder.listFiles()).max(Comparator.comparingLong(subFolder-> subFolder.lastModified()));

            if(!versionFolder.isPresent()){
                throw new DocumentStoreException("File not found :: " + fileIdentifier);
            }

            String folderName = versionFolder.get().getName();
            return Integer.parseInt(folderName.substring(1, folderName.length()));
        }catch (Exception e){
            //return first version
            return 1;
        }
    }

    /**
     * This method returns the File object of first copy of the document.
     * @param fileIdentifier
     * @return
     * @throws DocumentStoreException
     */
    public File getEarliestVersion(String fileIdentifier) throws DocumentStoreException{
        if(fileIdentifier == null || fileIdentifier.isEmpty()){
            throw new DocumentStoreException("Invalid file Identifier providedd :: " + fileIdentifier);
        }

        File versionFolder = new File(LocalStorageEngine.LOCAL_STORAGE_FOLDER + fileIdentifier + "/V1" );
        if(!versionFolder.exists()){
            throw new DocumentStoreException("File doesn't exist for the request version ");
        }

        return versionFolder.listFiles()[0];
    }

    /**
     * This method return the creation time of the document.
     * @param filePath
     * @return
     */
    public Timestamp getCreationTime(String filePath) {
        Path path = Paths.get(filePath);
        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
            Instant instant = Instant.ofEpochMilli(attr.creationTime().toMillis());
            return Timestamp.from(instant);
        } catch (IOException lException) {
            log.error("Error occured while getting creation time, setting to current time ::  ", lException);
        }
        return new Timestamp(System.currentTimeMillis());
    }
}
