package com.noteapp.user.service;

import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.user.dao.IAdminDAO;
import com.noteapp.user.dao.IUserDAO;
import com.noteapp.user.model.Admin;
import com.noteapp.user.model.Email;
import com.noteapp.user.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cung cấp các service liên quan tới User
 * @author Nhóm 17
 * @see IUserDAO
 * @see User
 */
public class UserService {
    protected IUserDAO userDAO;
    protected IAdminDAO adminDAO;
    
    public UserService(IUserDAO userDAO, IAdminDAO adminDAO) {
        this.userDAO = userDAO;
        this.adminDAO = adminDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setAdminDAO(IAdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }
    
    public boolean isAdmin(String username) throws UserServiceException {
        if (adminDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            adminDAO.get(username);
            return true;
        } catch (NotExistDataException ex1) {
            return false;
        } catch (DAOException ex2) {
            throw new UserServiceException(ex2.getMessage(), ex2.getCause());
        }
    }
    
    /**
     * Tạo một {@link User} mới và lưu và CSDL, đồng thời trả lại User vừa tạo
     * @param newUser User cần tạo
     * @return User được tạo thành công.
     * @throws UserServiceException Xảy ra khi (1) User đã tồn tại,
     * (2) Các câu lệnh không thể thực hiện do kết nối tới CSDL
     * @see IUserDAO#get(String) 
     * @see IUserDAO#create(User) 
     * @see com.noteapp.common.dao.FailedExecuteException
     */
    public User create(User newUser) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        String username = newUser.getUsername();
        //Kiểm tra đã tồn tại user hay chưa
        try {
            userDAO.get(username);
            throw new UserServiceException("This user is already exist!");
        } catch (NotExistDataException nedExForGet) {
            //Nếu chưa thì tiếp tục
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage(), exByGet.getCause());
        }
        //Tạo User mới và trả về
        try {
            return userDAO.create(newUser);
        } catch (DAOException exByCreate) {
            throw new UserServiceException(exByCreate.getMessage());
        }
    }
    
    /**
     * Kiểm tra tài khoản và mật khẩu để đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Các thông tin của User nếu tài khoản và mật khẩu đúng
     * @throws UserServiceException Xảy ra khi (1) User không tồn tại, 
     * (2) password sai, (3) Việc thực thi các câu lệnh lỗi
     * @see IUserDAO#get(String) 
     * @see com.noteapp.common.dao.DAOException
     */
    public User checkUser(String username, String password) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            //Lấy tài khoản
            User user = userDAO.get(username);
            //Kiểm tra mật khẩu
            if(password.equals(user.getPassword())) {
                return user;
            } else {
                throw new UserServiceException("Password is false!");
            }
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage(), exByGet.getCause());
        }
    }
    
    public Admin checkAdmin(String username, String password) throws UserServiceException {
        if (adminDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            //Lấy tài khoản
            Admin admin = adminDAO.get(username);
            //Kiểm tra mật khẩu
            if(password.equals(admin.getPassword())) {
                return admin;
            } else {
                throw new UserServiceException("Password is false!");
            }
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage(), exByGet.getCause());
        }
    }
    
    public boolean checkLocked(String username) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            //Lấy tài khoản
            User user = userDAO.get(username);
            //Kiểm tra mật khẩu
            return user.isLocked();
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage(), exByGet.getCause());
        }
    }
    
    public Map<String, Boolean> getAllLockedStatus() throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            List<User> users = userDAO.getAll();
            Map<String, Boolean> lockedStatus = new HashMap<>();
            for (User user: users) {
                lockedStatus.put(user.getUsername(), user.isLocked());
            }
            return lockedStatus;
        } catch (DAOException ex) {
            throw new UserServiceException(ex.getMessage(), ex.getCause());
        }
    }
    
    /**
     * Cập nhật mật khẩu của User
     * @param username username của user
     * @param newPassword password mới của user
     * @throws UserServiceException Xảy ra khi (1) User không tồn tại,
     * (2) Các câu lệnh bị lỗi
     * @see IUserDAO#get(String)
     * @see IUserDAO#update(User) 
     * @see com.noteapp.common.dao.DAOException
     */
    public void updatePassword(String username, String newPassword) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            User user = userDAO.get(username);
            user.setPassword(newPassword);
            userDAO.update(user);
        } catch (DAOException ex) {
            throw new UserServiceException(ex.getMessage(), ex.getCause());
        }
    }
    
    public void updateLockedStatus(String username, boolean isLocked) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            User user = userDAO.get(username);
            user.setLocked(isLocked);
            userDAO.update(user);
        } catch (DAOException ex) {
            throw new UserServiceException(ex.getMessage(), ex.getCause());
        }
    }
    
    /**
     * Cập nhật các thông tin của user
     * @param user User cần cập nhật
     * @return User sau khi cập nhật
     * @throws UserServiceException Xảy ra khi câu lệnh hoặc kết nối bị lỗi
     * @see IUserDAO#update(User)
     * @see com.noteapp.common.dao.DAOException
     */
    public User update(User user) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try {
            userDAO.update(user);
            return user;
        } catch (DAOException exByUpdate) {
            throw new UserServiceException(exByUpdate.getMessage());
        }
    }
    
    
    
    /**
     * Lấy địa chỉ email xác thực của User
     * @param username username của User cần lấy
     * @return Một {@link Email} chứa địa chỉ email xác thực của User
     * @throws UserServiceException Xảy ra khi không thể lấy dữ liệu của User
     * @see IUserDAO#get(String)
     * @see com.noteapp.common.dao.DAOException
     */
    public Email getVerificationEmail(String username) throws UserServiceException {
        if (userDAO == null) {
            throw new UserServiceException("DAO is null!");
        }
        try { 
            User user = userDAO.get(username);
            return user.getEmail();
        } catch (DAOException exByGet) {
            throw new UserServiceException(exByGet.getMessage());
        }
    }
}