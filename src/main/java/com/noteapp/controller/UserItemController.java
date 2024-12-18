package com.noteapp.controller;

import com.noteapp.user.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author admin
 */
public class UserItemController extends Controller {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label lockedStatusLabel;
    @FXML
    private Button changeLockStatusButton;
    
    public Button getChangeLockStatusButton() {
        return changeLockStatusButton;
    }
    
    public String getUsername() {
        return usernameLabel.getText();
    }
    
    public boolean isLocked() {
        return "Locked".equals(lockedStatusLabel.getText());
    }
    
    protected String getLockedStatus(boolean isLocked) {
        if (isLocked) {
            return "Locked";
        } else {
            return "Unlocked";
        }
    }
    
    public void changeLockedStatus() {
        if ("Locked".equals(lockedStatusLabel.getText())) {
            lockedStatusLabel.setText("Unlocked");
        } else {
            lockedStatusLabel.setText("Locked");
        }
    }

    public void setData(String username, boolean isLocked) {
        this.usernameLabel.setText(username);
        this.lockedStatusLabel.setText(getLockedStatus(isLocked));
    }
}