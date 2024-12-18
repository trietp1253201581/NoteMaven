package com.noteapp.user.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dao.DAOKeyException;
import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.user.model.Admin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Triển khai các phương thức thao tác dữ liệu với Admin
 * @author Nhóm 17
 */
public class AdminDAO implements IAdminDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/user/db/AdminQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";
    
    /**
     * Tên các cột trong CSDL
     */
    protected static enum ColumnName {
        username, password;
    }
    
    /**
     * Các loại Query được cung cấp
     */
    protected static enum QueriesType {
        GET, UPDATE;
    }
    
    private AdminDAO() {
        //init connection
        databaseConnection = new MySQLDatabaseConnection
            (DATABASE_HOST, DATABASE_PORT, DATABASE_NAME, 
                    DATABASE_USERNAME, DATABASE_PASSWORD);
        databaseConnection.connect();
        //Get enable Queries
        databaseConnection.readSQL(SQL_FILE_DIR);
        enableQueries = databaseConnection.getEnableQueries();
    }
    
    private static class SingletonHelper {
        private static final AdminDAO INSTANCE = new AdminDAO();
    }
    
    /**
     * Lấy thể hiện duy nhất của {@link AdminDAO}
     * @return Instance duy nhất
     * @see IAdminDAO
     */
    public static AdminDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Lấy và trả về một câu lệnh được chuẩn bị sẵn theo loại truy vấn
     * @param queriesType loại truy vấn
     * @return Một {@link PrepareStatement} là môt câu lệnh truy vấn SQL
     * đã được chuẩn bị sẵn tương ứng với loại truy vấn được yêu cầu
     * @throws SQLException Nếu kết nối tới CSDL không tồn tại
     */
    protected PreparedStatement getPrepareStatement(QueriesType queriesType) throws SQLException {
        //Kiểm tra kết nối
        if (databaseConnection.getConnection() == null) {
            throw new SQLException("Connection null!");
        }
        
        //Lấy query và truyền vào connection
        String query = enableQueries.get(queriesType.toString());
        return databaseConnection.getConnection().prepareStatement(query);
    }
    
    @Override
    public Admin get(String username) throws DAOException {
        //Nếu username không hợp lệ thì thông báo ngoại lệ Key
        if ("".equals(username)) {
            throw new DAOKeyException();
        }
        try {
            //Thực thi truy vấn SQL, gán tham số cho USERNAME và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.GET);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Lấy admin
            Admin admin = new Admin();
            while (resultSet.next()) {
                admin.setUsername(resultSet.getString(ColumnName.username.toString()));
                admin.setPassword(resultSet.getString(ColumnName.password.toString()));
            }
            if(admin.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return admin;
        } catch (SQLException ex) {
            throw new FailedExecuteException(ex.getCause());
        }
    }
    
    @Override
    public void update(Admin admin) throws DAOException {
        try {
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.UPDATE);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, admin.getPassword());
            preparedStatement.setString(2, admin.getUsername());
            
            //Nếu không hàng nào bị thay đổi thì thông báo lỗi
            if (preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException(ex.getCause());
        }
    }
}
