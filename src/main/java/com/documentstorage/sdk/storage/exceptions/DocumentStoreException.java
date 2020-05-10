package com.documentstorage.sdk.storage.exceptions;

public class DocumentStoreException extends Exception {
    /**
     * Default UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code> DocumentStoreException</code> with no detail message.
     */
    public DocumentStoreException() {
        super();
    }

    /**
     * Constructs Exception with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DocumentStoreException(String msg) {
        super(msg);
    }


    public DocumentStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentStoreException(Throwable cause) {
        super(cause);
    }

}
