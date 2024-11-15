/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.dao;

/**
 *
 * @author admin
 */
public class UnsupportedQueryException extends DAOException {

    public UnsupportedQueryException() {
        super("This query is unsupported with this DAO!");
    }
    
    public UnsupportedQueryException(String message) {
        super(message);
    }
    
}
