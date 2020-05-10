package com.documentstorage.sdk.storage.interfaces;

import com.documentstorage.sdk.storage.exceptions.DocumentStoreException;
import com.documentstorage.sdk.storage.pojo.DocumentMetadata;

/**
 * All the Storage engine should implement this interface
 */
public interface StorageEngine {

    /**
     * This method provides the name of storageEngine
     * @return : Storage engine name
     */
     String getStorageEngineName();

    /**
     * This method saves the document to a storageEngine instance.
     * @param absolutefilePath : Absolute file path from where file could be read.
     * @return : DocumentMetadata object having details about saved document.
     * @throws DocumentStoreException
     */
    DocumentMetadata saveDocument(String absolutefilePath) throws DocumentStoreException;

    /**
     * This method returns the Document metadata of latest version for the document id passed in request.
     * @param fileIdentifier : Document id to uniquely identify the document.
     * @return : DocumentMetadata object having details about saved document.
     * @throws DocumentStoreException
     */
    DocumentMetadata getDocumentInfo(String fileIdentifier) throws DocumentStoreException;

    /**
     * This method returns details about specific version of a document.
     * @param fileIdentifier : Document id to uniquely identify the document.
     * @param version : Version of the document.
     * @return : Metadata details of the requested version of the document.
     * @throws DocumentStoreException
     */
    DocumentMetadata getDocumentInfo(String fileIdentifier, int version) throws DocumentStoreException;

    /**
     * This method updates the requested document with the file details.
     * @param absoluteFilePath : File path from where document could be read.
     * @param fileIdentifier : Document id to uniquely identify the document.
     * @return : Metadata of the latest version of the updated document.
     * @throws DocumentStoreException
     */
    DocumentMetadata updateDocument(String absoluteFilePath, String fileIdentifier) throws DocumentStoreException;

    /**
     * This method updates the content of a particular version of the document
     * @param absoluteFilePath : Absoulte file path from where the document data could be read
     * @param fileIdentifier : A document id to uniquely identify the document.
     * @param version : Version of the document which needs to be updated
     * @return : DocumentMetadata of the updated document.
     * @throws DocumentStoreException
     */
    DocumentMetadata updateDocument(String absoluteFilePath, String fileIdentifier, int version) throws DocumentStoreException;

    /**
     * This method renames the document and stores it in a new version, earlier version is preserved as is.
     * @param fileIdentifier : document id to uniquely identify the document.
     * @param newName : New name of the file.
     * @return : Metadata of the renamed copy of the document.
     * @throws DocumentStoreException
     */
    DocumentMetadata renameDocument(String fileIdentifier, String newName) throws DocumentStoreException;

    /**
     * This method is used to rename a specific version of the document.
     * @param fileIdentifier : Docuemnt id to uniquely identify the document stored.
     * @param newName : New name of the document.
     * @param version : Version of the docuemnt which needs to be renamed.
     * @return : Document  metadata of the renamed document.
     * @throws DocumentStoreException
     */
    DocumentMetadata renameDocument(String fileIdentifier, String newName, int version) throws DocumentStoreException;

    /**
     * This methdo deletes the latest version of the document.
     * @param fileIdentifier : A document id to uniquely identify the document.
     * @return : True if document is successfully deleted, False if it fails.
     * @throws DocumentStoreException
     */
    boolean deleteDocument(String fileIdentifier) throws DocumentStoreException;

    /**
     * This method is used to delete a specific version of the document.
     * @param fileIdentifier : Document id to uniquely identify the document.
     * @param version : Version of the document which needs to be deleted.
     * @return True if delete is successfull ,False if it fails
     * @throws DocumentStoreException
     */
    boolean deleteDocument(String fileIdentifier, int version) throws DocumentStoreException;

}
