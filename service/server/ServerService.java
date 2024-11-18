package com.noteapp.service.server;

import com.noteapp.dao.DAOException;

/**
 * Định nghĩa các phương thức xử lý service bên server
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface ServerService<T> {   

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi
     */
    T execute() throws DAOException;   
}