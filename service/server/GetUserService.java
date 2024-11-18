package com.noteapp.service.server;

import com.noteapp.dao.BasicDAO;
import com.noteapp.dao.DAOException;
import com.noteapp.dao.NullKey;
import com.noteapp.dao.UserDAO;
import com.noteapp.dao.UserKey;
import com.noteapp.model.dto.User;

/**
 *
 * @author admin
 */
public class GetUserService implements ServerService<User> {
    private String username;
    protected BasicDAO<User, UserKey, NullKey> userDAO;

    public GetUserService() {
        this.username = "";
    }

    public GetUserService(String username) {
        this.username = username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public User execute() throws DAOException {
        userDAO = UserDAO.getInstance();
        return userDAO.get(new UserKey(username));
    }
}
