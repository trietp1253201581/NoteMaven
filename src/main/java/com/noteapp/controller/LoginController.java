package com.noteapp.controller;

import com.noteapp.user.dao.AdminDAO;
import com.noteapp.user.dao.UserDAO;
import com.noteapp.user.model.Admin;
import com.noteapp.user.model.User;
import com.noteapp.user.service.UserService;
import com.noteapp.user.service.UserServiceException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class cho Login GUI
 * 
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class LoginController extends InitableController {   
    //Các thuộc tính FXML
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;    
    @FXML 
    private Label registerLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Label forgotPasswordLabel;
    
    protected UserService userService;
        
    @Override
    public void init() {
        userService = new UserService(UserDAO.getInstance(), AdminDAO.getInstance());
        loginButton.setOnAction((ActionEvent event) -> {
            login();
        });
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        registerLabel.setOnMouseClicked((MouseEvent event) -> {
            RegisterController.open(stage);
        });
        forgotPasswordLabel.setOnMouseClicked((MouseEvent event) -> {
            ResetPasswordController.open(stage);
        });
    }

    protected void login() {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        //Kiểm tra thông tin đăng nhập
        try { 
            boolean isAdmin = userService.isAdmin(username);
            if (!isAdmin) {
                User user = userService.checkUser(username, password);
                showAlert(Alert.AlertType.INFORMATION, "Successfully Login");
                //Mở Dashboard của user này
                DashboardController.open(user, stage);
            } else {
                userService.checkAdmin(username, password);
                showAlert(Alert.AlertType.INFORMATION, "Successfully Login");
                //Mở Dashboard của user này
                AdminDashboardController.open(stage);
            }
            
        } catch (UserServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    public static void open(Stage stage) {
        try {
            String filePath = Controller.DEFAULT_FXML_RESOURCE + "LoginView.fxml";
            
            LoginController controller = new LoginController();

            controller.setStage(stage);
            controller.loadFXMLAndSetScene(filePath, controller);
            controller.init();
            //Set scene cho stage và show
            
            controller.showFXML();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open login");
        }
    }
}