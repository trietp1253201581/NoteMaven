package com.noteapp.dao;

import com.noteapp.model.User;
import com.noteapp.model.Email;
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
public class UserDAO extends DAO<User> {
    protected static final String USER_QUERIES_FILE_NAME = "UserQueries.sql";
    
    protected static enum ColumnName {
        name, username, password, birthday, school, gender, email;
    }

    private UserDAO() {
        super.sqlFileName = USER_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
    }
    
    private static class SingletonHelper {
        private static final UserDAO INSTANCE = new UserDAO();
    }
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static UserDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override
    public List<User> getAll() throws DAOException {
        List<User> users = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL.toString());

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
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

                users.add(user);
            }    
            return users;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public List<User> getAll(DAOKey referKey) throws DAOException {
        return this.getAll();
    }

    @Override
    public User get(DAOKey key) throws DAOException {
        User user = new User();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.username.toString())) {
            throw new DAOKeyException();
        }
        String username = keyMap.get(ColumnName.username.toString());
        try {
            //Thực thi truy vấn SQL, gán tham số cho USERNAME và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

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
            if(user.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return user;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public User create(User newUser) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, newUser.getName());
            preparedStatement.setString(2, newUser.getUsername());
            preparedStatement.setString(3, newUser.getPassword());
            preparedStatement.setDate(4, newUser.getBirthday());
            preparedStatement.setString(5, newUser.getSchool());
            preparedStatement.setString(6, newUser.getGender().toString());
            preparedStatement.setString(7, newUser.getEmail().getAddress());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newUser;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public User create(User newUser, DAOKey key) throws DAOException {
        return this.create(newUser);
    }

    @Override
    public void update(User user) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE.toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getSchool());
            preparedStatement.setString(5, user.getGender().toString());
            preparedStatement.setString(6, user.getEmail().getAddress());
            preparedStatement.setString(7, user.getUsername());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(User user, DAOKey key) throws DAOException {
        this.update(user);
    }

    @Override
    public void delete(DAOKey key) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.username.toString())) {
            throw new DAOKeyException();
        }
        String username = keyMap.get(ColumnName.username.toString());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, username);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public void deleteAll(DAOKey referKey) throws DAOException {
        throw new UnsupportedQueryException();
    }
}