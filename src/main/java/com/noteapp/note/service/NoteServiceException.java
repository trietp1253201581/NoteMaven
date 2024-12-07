/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.note.service;

/**
 *
 * @author admin
 */
public class NoteServiceException extends Exception {
    
    protected static final String NOTIFY = "Cause by Note App System. ";

    public NoteServiceException(String message) {
        super(NOTIFY + message);
    }
    
}
