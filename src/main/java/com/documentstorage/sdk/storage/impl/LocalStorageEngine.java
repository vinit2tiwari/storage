package com.documentstorage.sdk.storage.impl;


import com.documentstorage.sdk.storage.exceptions.DocumentStoreException;
import com.documentstorage.sdk.storage.interfaces.StorageEngine;
import com.documentstorage.sdk.storage.pojo.DocumentMetadata;
import com.documentstorage.sdk.storage.pojo.FileNameInfo;
import com.documentstorage.sdk.storage.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

/**
 * This class is responsible for storing file locally and perform operations on it
 */
@Service
@Slf4j
public class LocalStorageEngine implements StorageEngine {

    public static final String LOCAL_STORAGE_FOLDER  = System.getProperty("user.dir") + "/uploads/";
    public static final String LOCAL_STORAGE = "LOCAL-STORAGE";

    @Autowired
    private FileUtil fileUtil;

    @Override
    public String getStorageEngineName() {
        return LOCAL_STORAGE;
    }

    @Override
    public DocumentMetadata saveDocument(String  absoluteFilePath) throws DocumentStoreException {
        try {
            FileInputStream lipStream = new FileInputStream(new File(absoluteFilePath));
            String lstrFileName = fileUtil.getCanonicalName(absoluteFilePath);
            String directoryName = String.valueOf(System.currentTimeMillis());

            directoryName = fileUtil.createChildDir(directoryName);

            //Create a child directory to store version specific files
            File versionDir = new File(LOCAL_STORAGE_FOLDER + directoryName + "/V1");
            versionDir.mkdir();

            String lOutputFile = LOCAL_STORAGE_FOLDER + directoryName + "/V1/" + lstrFileName;
            OutputStream lOutputStream = new FileOutputStream(lOutputFile);
            int c;

            while ((c = lipStream.read()) != -1) {
                lOutputStream.write(c);
            }

            lipStream.close();
            lOutputStream.close();

            return getDocumentInfo(directoryName);
        } catch (FileNotFoundException exp) {
            log.error("File not found :: ", exp);
        } catch (IOException lException) {
            log.error("Error occured while saving document :: ", lException);
        }
        return null;
    }

    @Override
    public DocumentMetadata getDocumentInfo(String fileIdentifier) throws DocumentStoreException {
        if (fileIdentifier != null) {
            int latestVersion = fileUtil.getLatestVersion(fileIdentifier);

            return getDocumentInfo(fileIdentifier , latestVersion);
        }
        throw new DocumentStoreException("Invalid file identifier provided :: " + fileIdentifier);
    }

    @Override
    public DocumentMetadata getDocumentInfo(String fileIdentifier, int version) throws DocumentStoreException {
        if(fileIdentifier == null || fileIdentifier.isEmpty()){
            throw new DocumentStoreException("Invalid file Identifier providedd :: " + fileIdentifier);
        }

        File versionFolder = new File(LOCAL_STORAGE_FOLDER + fileIdentifier + "/V" + version);
        if(!versionFolder.exists()){
            throw new DocumentStoreException("File doesn't exist for the request version ");
        }

        File file = versionFolder.listFiles()[0];

        File earliestVersion = fileUtil.getEarliestVersion(fileIdentifier);
        DocumentMetadata lDocumentMetadata = new DocumentMetadata();

        //Since version is updated we need to keep V1 time as creation time
        lDocumentMetadata.setCreationTime(fileUtil.getCreationTime(earliestVersion.getAbsolutePath()));
        FileNameInfo lFileNameInfo = fileUtil.getFileName(file.getName());
        lDocumentMetadata.setDocumentId(fileIdentifier);
        lDocumentMetadata.setDocumentName(lFileNameInfo.getFileName());
        lDocumentMetadata.setLastModifiedTime(new Timestamp(file.lastModified()));
        lDocumentMetadata.setSize(file.getTotalSpace());
        lDocumentMetadata.setVersion(version);
        return lDocumentMetadata;
    }

    @Override
    public DocumentMetadata updateDocument(String absoluteFilePath, String fileIdentifier)
            throws DocumentStoreException {
        //While updating the document update the latest version by 1.
        int version = fileUtil.getLatestVersion(fileIdentifier) + 1;

        return updateDocument(absoluteFilePath, fileIdentifier , version) ;

    }

    @Override
    public DocumentMetadata updateDocument(String absoluteFilePath, String fileIdentifier, int version)
            throws DocumentStoreException {
        return updateDocument(absoluteFilePath, fileIdentifier, version , fileUtil.getCanonicalName(absoluteFilePath));
    }

    private  DocumentMetadata updateDocument(String absoluteFilePath, String fileIdentifier, int version , String newName) throws DocumentStoreException{
        if(fileIdentifier == null || fileIdentifier.isEmpty()){
            throw new DocumentStoreException("Invalid file Identifier providedd :: " + fileIdentifier);
        }

        File latestVersion = new File(LOCAL_STORAGE_FOLDER + fileIdentifier + "/V" + version);

        if(!latestVersion.mkdir()){
            throw new DocumentStoreException("Error occured while storing new Version, please try again " + fileIdentifier + " :: " + version);
        }

        try{
            FileInputStream lipStream = new FileInputStream(new File(absoluteFilePath));
            OutputStream lOutputStream = new FileOutputStream(latestVersion.getAbsolutePath() + "/" + newName);
            int c;

            while ((c = lipStream.read()) != -1) {
                lOutputStream.write(c);
            }

            lipStream.close();
            lOutputStream.close();

            return getDocumentInfo(fileIdentifier , version);
        }catch (FileNotFoundException exp){
            throw new DocumentStoreException("Error occured while storing new version " + absoluteFilePath + " :: " + version , exp);
        } catch (IOException e) {
            throw new DocumentStoreException("Error occured while storing new version " + absoluteFilePath + " :: " + version , e);
        }
    }

    @Override
    public DocumentMetadata renameDocument(String fileIdentifier, String newName) throws DocumentStoreException {
        int version = fileUtil.getLatestVersion(fileIdentifier);
        return renameDocument(fileIdentifier , newName , version);
    }

    @Override
    public DocumentMetadata renameDocument(String fileIdentifier, String newName, int version)
            throws DocumentStoreException {

        File versionFolder = new File(LOCAL_STORAGE_FOLDER+ fileIdentifier + "/V" + version);

        File lOldFile = versionFolder.listFiles()[0];

        String fileExtension = fileUtil.getFileName(lOldFile.getAbsolutePath()).getFileExtension();
        return updateDocument(lOldFile.getAbsolutePath() , fileIdentifier , version +1 , newName + fileExtension);
    }

    @Override
    public boolean deleteDocument(String fileIdentifier) throws DocumentStoreException {
        int version = fileUtil.getLatestVersion(fileIdentifier);
        return deleteDocument(fileIdentifier , version);
    }

    @Override
    public boolean deleteDocument(String fileIdentifier, int version) throws DocumentStoreException {
        File lfile = new File(LOCAL_STORAGE_FOLDER + fileIdentifier + "/V" + version);
        return FileSystemUtils.deleteRecursively(lfile);
    }
}
