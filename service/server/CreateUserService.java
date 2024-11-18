package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.FailedExecuteException;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.NullKey;
import com.noteapp.dao.UserDAO;
import com.noteapp.dao.UserKey;
import com.noteapp.model.dto.User;
import com.noteapp.dao.BasicDAO;

/**
 * Tạo một User mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateUserService implements ServerService<User> {    
    private User user;
    protected BasicDAO<User, UserKey, NullKey> userDataAccess;
    
    public CreateUserService() {
        user = new User();
    }

    public CreateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DAOException {       
        //Tạo một đối tượng access dữ liệu
        userDataAccess = UserDAO.getInstance();
        try {
            userDataAccess.get(new UserKey(user.getUsername()));
            throw new DAOException("This user is already exist!");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            return userDataAccess.add(user);
        }
    }    
}