package com.noteapp.dao;

/**
 *
 * @author admin
 */
public class DAOKeyException extends DAOException {

    public DAOKeyException() {
        super("This key is not accepted!");
    }
    
    public DAOKeyException(String message) {
        super(message);
    }
}
