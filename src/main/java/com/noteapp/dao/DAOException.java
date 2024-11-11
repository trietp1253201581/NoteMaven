package com.noteapp.dao;

/**
 * Ngoại lệ cho việc access dữ liệu
 * @author Nhóm 23
 * @since 23/05/2024
 * @version 1.0
 */
public class DAOException extends Exception {
    public DAOException(String message) {
        super(message);
    }
}