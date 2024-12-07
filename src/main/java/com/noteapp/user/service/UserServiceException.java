/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.user.service;

/**
 *
 * @author admin
 */
public class UserServiceException extends Exception {
    
    protected static final String NOTIFY = "Cause by Note App System. ";

    public UserServiceException(String message) {
        super(NOTIFY + message);
    }
    
}
