package com.noteapp.dataaccess.connection;

import java.sql.Connection;

/**
 * Định nghĩa phương thức để connect tới CSDL
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface DatabaseConnection {

    /**
     * Lấy kết nối JDBC
     * @return Đối tượng {@code Connection} connect tới cơ sở dữ liệu
     */
    Connection getConnection();
}