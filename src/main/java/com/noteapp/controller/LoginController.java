package com.noteapp.controller;

import com.noteapp.model.User;
import com.noteapp.service.server.ServerServiceException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class cho Login GUI
 * 
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class LoginController extends Controller {   
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
        
    @Override
    public void init() {
        initServerService();
        loginButton.setOnAction((ActionEvent event) -> {
            login();
        });
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        registerLabel.setOnMouseClicked((MouseEvent event) -> {
            openRegister();
        });
        forgotPasswordLabel.setOnMouseClicked((MouseEvent event) -> {
            openResetPasswordView();
        });
    }

    protected void login() {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        //Kiểm tra thông tin đăng nhập
        try { 
            User user = userService.checkPassword(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Successfully Login");
            //Mở Dashboard của user này
            openDashboard(user);
        } catch (ServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    protected void openEditNoteView(User user) {
        
    }
    
    protected void openRegister() {
        try {
            String filePath = Controller.DEFAULT_FXML_RESOURCE + "RegisterView.fxml";
            
            RegisterController controller = new RegisterController();

            controller.setStage(stage);
            controller.loadFXMLAndSetScene(filePath, controller);
            controller.init();
            //Set scene cho stage và show
            
            controller.showFXML();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open register");
        }
    }
    
    protected void openResetPasswordView() { 
        try {
            String filePath = Controller.DEFAULT_FXML_RESOURCE + "ResetPasswordView.fxml";
            
            ResetPasswordController controller = new ResetPasswordController();

            controller.setStage(stage);
            controller.loadFXMLAndSetScene(filePath, controller);
            controller.init();
            //Set scene cho stage và show
            
            controller.showFXML();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Can't open reset Password");
        }
    }
    
    protected void openDashboard(User myUser) {
        try {
            String filePath = Controller.DEFAULT_FXML_RESOURCE + "DashboardView.fxml";
            
            DashboardController controller = new DashboardController();

            controller.setStage(stage);
            controller.setMyUser(myUser);
            controller.loadFXMLAndSetScene(filePath, controller);
            controller.init();
            //Set scene cho stage và show
            
            controller.showFXML();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open dashboard");
        }
    }
        
}