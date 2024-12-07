package com.noteapp.common.dao;

/**
 * Ngoại lệ không thể thực thi câu lệnh SQL
 * @author Nhóm 23
 * @since 23/05/2024
 * @version 1.0
 */
public class FailedExecuteException extends DAOException {
    public FailedExecuteException() {
        super("Can't execute!");
    }

    public FailedExecuteException(Throwable cause) {
        super("Can't execute!", cause);
    }
    
    public FailedExecuteException(String message) {
        super(message);
    }

    public FailedExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}