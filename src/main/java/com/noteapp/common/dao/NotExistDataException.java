package com.noteapp.common.dao;

/**
 * Ngoại lệ cho việc dữ liệu không tồn tại
 * @author Nhóm 23
 * @since 23/05/2024
 * @version 1.0
 */
public class NotExistDataException extends DAOException {  
    public NotExistDataException() {
        super("Data is not exist");
    }
    
    public NotExistDataException(String message) {
        super(message);
    }

    public NotExistDataException(Throwable cause) {
        super("Data is not exist", cause);
    }

    public NotExistDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
