package com.noteapp.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Triển khai phương thức tới CSDL
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class MySQLDatabaseConnection extends DatabaseConnection {

    public MySQLDatabaseConnection(String url, String username, String password) {
        super.url = url;
        super.username = username;
        super.password = password;
        super.enableQueries = new HashMap<>();
    }
    
    public MySQLDatabaseConnection(String host, int port, String database, String username, String password) {
        url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        super.username = username;
        super.password = password;
        super.enableQueries = new HashMap<>();
    }
    
    /**
     * Lấy kết nối JDBC
     * @return Đối tượng {@code Connection} connect tới cơ sở dữ liệu MySQL
     */
    @Override
    public Connection getConnection() {
        //Gọi đối tượng Driver để kết nối
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
        //Tạo 1 connection
        Connection connection = null;
        //Connect tới database với các thông tin đã cho
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println(e);
        }
        //Trả về connection
        return connection;
    }
}