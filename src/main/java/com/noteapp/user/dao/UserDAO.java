package com.noteapp.user.dao;

import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.common.dao.DAOKeyException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.user.model.User;
import com.noteapp.user.model.Email;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các phương thức thao tác dữ liệu với User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UserDAO implements IUserDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/user/db/UserQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";
    
    /**
     * Tên các cột trong CSDL
     */
    protected static enum ColumnName {
        name, username, password, birthday, school, gender, email;
    }
    
    /**
     * Các loại Query được cung cấp
     */
    protected static enum QueriesType {
        GET_ALL, GET, CREATE, UPDATE, DELETE;
    }

    private UserDAO() {
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
        private static final UserDAO INSTANCE = new UserDAO();
    }
    
    /**
     * Lấy thể hiện duy nhất của {@link UserDAO}
     * @return Instance duy nhất
     * @see IUserDAO
     */
    public static UserDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
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
    public List<User> getAll() throws DAOException {
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            List<User> users = new ArrayList<>();
            //Chuyển từng hàng dữ liệu sang user và thêm vào list
            while (resultSet.next()) {
                User user = new User();
                Email email = new Email();
                //Set dữ liệu cho user
                user.setName(resultSet.getString(ColumnName.name.toString()));
                user.setUsername(resultSet.getString(ColumnName.username.toString()));
                user.setPassword(resultSet.getString(ColumnName.password.toString()));
                user.setBirthday(resultSet.getDate(ColumnName.birthday.toString()));
                user.setSchool(resultSet.getString(ColumnName.school.toString()));
                user.setGender(User.Gender.valueOf(resultSet.getString(ColumnName.gender.toString())));
                email.setAddress(resultSet.getString(ColumnName.email.toString()));
                user.setEmail(email);
                //Thêm vào list các user
                users.add(user);
            }    
            return users;
        } catch (SQLException sqlException) {
            throw new FailedExecuteException(sqlException.getCause());
        }
    }

    @Override
    public User get(String username) throws DAOException {
        //Nếu username không hợp lệ thì thông báo ngoại lệ Key
        if ("".equals(username)) {
            throw new DAOKeyException();
        }
        try {
            //Thực thi truy vấn SQL, gán tham số cho USERNAME và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.GET);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Lấy User
            User user = new User();
            while (resultSet.next()) {
                Email email = new Email();
                //Set dữ liệu từ hàng nhận được
                user.setName(resultSet.getString(ColumnName.name.toString()));
                user.setUsername(resultSet.getString(ColumnName.username.toString()));
                user.setPassword(resultSet.getString(ColumnName.password.toString()));
                user.setBirthday(resultSet.getDate(ColumnName.birthday.toString()));
                user.setSchool(resultSet.getString(ColumnName.school.toString()));
                user.setGender(User.Gender.valueOf(resultSet.getString(ColumnName.gender.toString())));
                email.setAddress(resultSet.getString(ColumnName.email.toString()));
                user.setEmail(email);
            }
            
            //Nếu ko tồn tại thì ném ra ngoại lệ
            if (user.isDefaultValue()) {
                throw new NotExistDataException();
            }
            
            return user;
        } catch (SQLException sqlException) {
            throw new FailedExecuteException(sqlException.getCause());
        }       
    }

    @Override
    public User create(User newUser) throws DAOException {
        try {
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.CREATE);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, newUser.getName());
            preparedStatement.setString(2, newUser.getUsername());
            preparedStatement.setString(3, newUser.getPassword());
            preparedStatement.setDate(4, newUser.getBirthday());
            preparedStatement.setString(5, newUser.getSchool());
            preparedStatement.setString(6, newUser.getGender().toString());
            preparedStatement.setString(7, newUser.getEmail().getAddress());
            
            //Nếu không hàng nào được tạo mới thì thông báo lỗi
            if (preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            
            return newUser;
        } catch (SQLException sqlException) {
            throw new FailedExecuteException(sqlException.getCause());
        }
    }

    @Override
    public void update(User user) throws DAOException {
        try {
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.UPDATE);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getSchool());
            preparedStatement.setString(5, user.getGender().toString());
            preparedStatement.setString(6, user.getEmail().getAddress());
            preparedStatement.setString(7, user.getUsername());
            
            //Nếu không hàng nào bị thay đổi thì thông báo lỗi
            if (preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException sqlException) {
            throw new FailedExecuteException(sqlException.getCause());
        }
    }

    @Override
    public void delete(String username) throws DAOException {
        //Kiểm tra tính hợp lệ của username
        if ("".equals(username)) {
            throw new DAOKeyException();
        }
        try {
            PreparedStatement preparedStatement = getPrepareStatement(QueriesType.DELETE);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, username);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException sqlException) {
            throw new FailedExecuteException(sqlException.getCause());
        }
    }
}