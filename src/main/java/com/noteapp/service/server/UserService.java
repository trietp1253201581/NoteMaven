package com.noteapp.service.server;

import com.noteapp.dao.DAO;
import com.noteapp.dao.DAOException;
import com.noteapp.dao.DAOKey;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.UserDAO;
import com.noteapp.model.Email;
import com.noteapp.model.User;

/**
 *
 * @author admin
 */
public class UserService {
    protected DAO<User> userDAO;

    public UserService() {
        
    }
    
    protected void getInstanceOfDAO() {
        userDAO = UserDAO.getInstance();
    }
    
    public User create(User newUser) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "username";
        key.addKey(keyName, newUser.getUsername());
        try {
            userDAO.get(key);
        } catch (NotExistDataException nedExForGet) {
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
        try {
            return userDAO.create(newUser);
        } catch (DAOException exByCreate) {
            throw new ServerServiceException(exByCreate.getMessage());
        }
    }
    
    public User checkPassword(String username, String password) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "username";
        key.addKey(keyName, username);
        try {
            User user = userDAO.get(key);
            if(password.equals(user.getPassword())) {
                return user;
            } else {
                throw new ServerServiceException("Password is false!");
            }
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
    }
    
    public void updatePassword(String username, String newPassword) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "username";
        key.addKey(keyName, username);
        try {
            User user = userDAO.get(key);
            user.setPassword(newPassword);
            userDAO.update(user);
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
    }
    
    public User update(User user) throws ServerServiceException {
        getInstanceOfDAO();
        try {
            userDAO.update(user);
            return user;
        } catch (DAOException exByUpdate) {
            throw new ServerServiceException(exByUpdate.getMessage());
        }
    }
    
    public Email checkEmail(String username) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "username";
        key.addKey(keyName, username);
        try { 
            User user = userDAO.get(key);
            return user.getEmail();
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
    }
}