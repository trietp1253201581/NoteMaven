package com.noteapp.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Connection tới hệ QT CSDL MySQL
 * @author admin
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
    
    @Override
    public Connection getConnection() {
        //Gọi đối tượng Driver để kết nối
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        //Tạo 1 connection
        Connection connection = null;
        //Connect tới database với các thông tin đã cho
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        //Trả về connection
        return connection;
    }
}