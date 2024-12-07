package com.noteapp.user.service;

import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.user.dao.IUserDAO;
import com.noteapp.user.dao.UserDAO;
import com.noteapp.user.model.Email;
import com.noteapp.user.model.User;

/**
 *
 * @author admin
 */
public class UserService {
    protected IUserDAO userDAO;

    public UserService() {
        
    }
    
    protected void getInstanceOfDAO() {
        userDAO = UserDAO.getInstance();
    }
    
    public User create(User newUser) throws UserServiceException {
        getInstanceOfDAO();
        String username = newUser.getUsername();
        try {
            userDAO.get(username);
        } catch (NotExistDataException nedExForGet) {
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage());
        }
        try {
            return userDAO.create(newUser);
        } catch (DAOException exByCreate) {
            throw new UserServiceException(exByCreate.getMessage());
        }
    }
    
    public User checkPassword(String username, String password) throws UserServiceException {
        getInstanceOfDAO();
        try {
            User user = userDAO.get(username);
            if(password.equals(user.getPassword())) {
                return user;
            } else {
                throw new UserServiceException("Password is false!");
            }
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage());
        }
    }
    
    public void updatePassword(String username, String newPassword) throws UserServiceException {
        getInstanceOfDAO();
        try {
            User user = userDAO.get(username);
            user.setPassword(newPassword);
            userDAO.update(user);
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage());
        }
    }
    
    public User update(User user) throws UserServiceException {
        getInstanceOfDAO();
        try {
            userDAO.update(user);
            return user;
        } catch (DAOException exByUpdate) {
            throw new UserServiceException(exByUpdate.getMessage());
        }
    }
    
    public Email getVerificationEmail(String username) throws UserServiceException {
        getInstanceOfDAO();
        try { 
            User user = userDAO.get(username);
            return user.getEmail();
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage());
        }
    }
}