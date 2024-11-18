/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.service.server;

/**
 *
 * @author admin
 */
public class ServerServiceException extends Exception {
    
    protected static final String NOTIFY = "Cause by Note App System. ";

    public ServerServiceException(String message) {
        super(NOTIFY + message);
    }
    
}
