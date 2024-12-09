package com.noteapp.note.service;

/**
 * Một ngoại lệ cho các dịch vụ liên quan tới Note
 * @author admin
 */
public class NoteServiceException extends Exception {

    public NoteServiceException(String message) {
        super(message);
    }

    public NoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}