package com.noteapp.dao;

import com.noteapp.dao.connection.DatabaseConnection;
import com.noteapp.dao.connection.MySQLDatabaseConnection;
import com.noteapp.model.DTO;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object.
 * Một Object giúp chuyển đổi CSDL từ hệ QT CSDL
 * sang ứng dụng
 * @author Nhóm 17
 * @param <T> Một class con của {@link DTO}
 * @version 1.0
 * @see DTO
 */
public abstract class DAO<T extends DTO> {
    protected Connection connection;
    protected DatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    protected String sqlFileName;
    
    protected static final String DEFAULT_SQL_DIR = "src/main/java/com/noteapp/db/";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";
        
    /**
     * Các loại Query được phép sử dụng
     */
    protected static enum QueriesType {
        GET_ALL, GET_ALL_REFER, 
        GET, 
        CREATE, CREATE_KEY,
        UPDATE, UPDATE_KEY,
        DELETE, DELETE_ALL;
    }
    
    /**
     * Khởi tạo Connection tới hệ quản trị CSDL
     * @see DatabaseConnection
     */
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
    
    /**
     * Lấy các query từ các file SQL tương ứng.
     * @see DatabaseConnection#readSQL
     */
    protected void initEnableQueries() {
        databaseConnection.readSQL(DEFAULT_SQL_DIR + sqlFileName);
        this.enableQueries = databaseConnection.getEnableQueries();
    }
    
    /**
     * Lấy một bản ghi từ CSDL sử dụng key
     * @param key key tương ứng cho từng loại dữ liệu
     * @return {@link DTO} tương ứng của bản ghi
     * @throws DAOException nếu không thể thực hiện truy vấn 
     * hoặc dữ liệu không tồn tại
     * @see DAOKey
     */
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