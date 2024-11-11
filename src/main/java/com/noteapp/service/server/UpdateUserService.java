package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.NullKey;
import com.noteapp.dao.UserDAO;
import com.noteapp.dao.UserKey;
import com.noteapp.model.datatransfer.User;
import com.noteapp.dao.BasicDAO;


/**
 * Cập nhật User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UpdateUserService implements ServerService<User> {    
    private User user;
    protected BasicDAO<User, UserKey, NullKey> userDataAccess;
    
    public UpdateUserService() {
        user = new User();
    }

    public UpdateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DAOException {
        userDataAccess = UserDAO.getInstance();
        userDataAccess.update(user);
        return user;
    }    
}