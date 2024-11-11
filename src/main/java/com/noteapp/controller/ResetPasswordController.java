package com.noteapp.controller;

import com.noteapp.dao.DAOException;
import com.noteapp.model.datatransfer.Email;
import com.noteapp.model.datatransfer.User;
import com.noteapp.service.security.MailjetSevice;
import com.noteapp.service.security.SixNumVerificationCodeService;
import com.noteapp.service.server.GetUserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ResetPasswordController extends Controller {
    @FXML
    private Button closeButton;
    @FXML
    private Button confirmPasswordButton;
    @FXML
    private Label errorUsernameFieldLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button sendCodeButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField verificationCodeField;
    @FXML
    private Button verifyCodeButton;

    @Override
    public void init() {
        initScene();
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        }); 
        sendCodeButton.setOnAction((ActionEvent event) -> {
            sendCode();
        }); 
        verifyCodeButton.setOnAction((ActionEvent event) -> {
            checkVerifyCode();
        }); 
    }
    
    protected void initScene() {
        errorUsernameFieldLabel.setVisible(false);
        verificationCodeField.setEditable(false);
        passwordField.setEditable(false);
    }
    
    protected void sendCode() {
        String username = usernameField.getText();
        if("".equals(username)) {
            errorUsernameFieldLabel.setVisible(true);
            return;
        } else {
            errorUsernameFieldLabel.setVisible(false);
        }
        userService = new GetUserService(username);
        try {
            User thisUser = userService.execute();
            Email thisEmail = thisUser.getEmail();
            mailService = new MailjetSevice();
            String subject = "Verification Code for " + username;
            verificationCodeService = new SixNumVerificationCodeService();
            verificationCodeService.generateVerificationCode();
            String verificationCode = verificationCodeService.getVerificationCode();
            String content = "Your verification code is " + verificationCode;
            mailService.sendMail(thisEmail, subject, content);
            verificationCodeField.setEditable(true);
            showAlert(Alert.AlertType.INFORMATION, "Your code is sent");
        } catch (DAOException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void checkVerifyCode() {
        if(verificationCodeService == null) {
            return;
        }
        String verifyCode = verificationCodeField.getText();
        boolean isExpiredCode = verificationCodeService.isExpiredCode();
        boolean checkCode = verificationCodeService.verifyCode(verifyCode);
        if (isExpiredCode) {
            showAlert(Alert.AlertType.ERROR, "Your code is expired!");
            verificationCodeField.setEditable(false);
        } else if (!checkCode) {
            showAlert(Alert.AlertType.ERROR, "Your code is false!");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Please input your new password!");
            passwordField.setEditable(true);
        }
    }
}
