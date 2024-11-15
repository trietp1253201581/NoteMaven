package com.noteapp.dao;

import com.noteapp.dao.connection.DatabaseConnection;
import com.noteapp.dao.connection.MySQLDatabaseConnection;
import com.noteapp.model.DTO;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Định nghĩa các phương thức thao tác cơ bản với CSDL
 * @author Nhóm 23
 * @param <T> Kiểu datatransfer cho data từ CSDL
 * @since 30/03/2024
 * @version 1.0
 */
public abstract class DAO<T extends DTO> {
    protected Connection connection;
    protected DatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    protected String sqlFileName;
    
    protected static final String PACKAGE_PATH = "src/main/java/com/noteapp/db/";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";
        
    protected static enum QueriesType {
        GET_ALL, GET_ALL_REFER, 
        GET, 
        CREATE, CREATE_KEY,
        UPDATE, UPDATE_KEY,
        DELETE, DELETE_ALL;
    }
    
    protected void initConnection() {
        String host = DATABASE_HOST;
        int port = DATABASE_PORT;
        String dbName = DATABASE_NAME;
        String username = DATABASE_USERNAME;
        String password = DATABASE_PASSWORD;
        databaseConnection = new MySQLDatabaseConnection
            (host, port, dbName, username, password);
        this.connection = databaseConnection.getConnection();
    }
    
    protected void initEnableQueries() {
        databaseConnection.readSQL(PACKAGE_PATH + sqlFileName);
        this.enableQueries = databaseConnection.getEnableQueries();
    }
    
    public abstract T get(DAOKey key) throws DAOException;
    
    public abstract T create(T element) throws DAOException;
    public abstract T create(T element, DAOKey key) throws DAOException;
    
    public abstract void update(T element) throws DAOException;
    public abstract void update(T element, DAOKey key) throws DAOException;
    
    public abstract void delete(DAOKey key) throws DAOException;
    public abstract void deleteAll(DAOKey referKey) throws DAOException;
    
    public abstract List<T> getAll() throws DAOException;
    public abstract List<T> getAll(DAOKey referKey) throws DAOException;
}