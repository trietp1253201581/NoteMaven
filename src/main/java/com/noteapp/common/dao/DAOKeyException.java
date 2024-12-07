package com.noteapp.common.dao;

/**
 *
 * @author admin
 */
public class DAOKeyException extends DAOException {
    public DAOKeyException() {
        super("This key is not accepted!");
    }

    public DAOKeyException(Throwable cause) {
        super("This key is not accepted!", cause);
    }

    public DAOKeyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DAOKeyException(String message) {
        super(message);
    }
}
