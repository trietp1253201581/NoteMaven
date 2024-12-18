package com.noteapp.controller;

import com.noteapp.user.dao.AdminDAO;
import com.noteapp.user.dao.UserDAO;
import com.noteapp.user.service.UserService;
import com.noteapp.user.service.UserServiceException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class AdminDashboardController extends InitableController {
    @FXML
    private Button viewUsersButton;
    @FXML
    private TextField searchUserField;
    @FXML
    private VBox userCardLayout;
    @FXML
    private Button closeButton;
    @FXML
    private Button logoutButton;
    
    protected UserService userService;
    protected Map<String, Boolean> lockedStatusOfUsers;
    
    @Override
    public void init() {
        userService = new UserService(UserDAO.getInstance(), AdminDAO.getInstance());
        initView();
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        logoutButton.setOnAction((ActionEvent event) -> {
            LoginController.open(stage);
        });
        viewUsersButton.setOnAction((ActionEvent event) -> {
            initView();
        });
        searchUserField.setOnAction((ActionEvent event) -> {
            searchUser();
        });
    }
    
    public void initView() {
        try {
            lockedStatusOfUsers = userService.getAllLockedStatus();
            loadUsers(lockedStatusOfUsers);
        } catch (UserServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
        
    }
    
    public void loadUsers(Map<String, Boolean> lockedStatusOfUsers) {
        userCardLayout.getChildren().clear();
        if (lockedStatusOfUsers.isEmpty()) {
            return;
        }
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "UserItemView.fxml";
        for (Map.Entry<String, Boolean> entry: lockedStatusOfUsers.entrySet()) {
            String username = entry.getKey();
            boolean isLocked = entry.getValue();
            
            try {
                UserItemController controller = new UserItemController();
                HBox box = controller.loadFXML(filePath, controller);
                controller.setData(username, isLocked);
                controller.getChangeLockStatusButton().setOnAction((ActionEvent event) -> {
                    controller.changeLockedStatus();
                    String thisUsername = controller.getUsername();
                    boolean thisLockedStatus = controller.isLocked();
                    this.lockedStatusOfUsers.put(thisUsername, thisLockedStatus);
                    try {
                        userService.updateLockedStatus(thisUsername, thisLockedStatus);
                    } catch (UserServiceException ex) {
                        System.err.println(ex.getCause());
                    }
                });
                userCardLayout.getChildren().add(box);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    protected void searchUser() {
        String searchText = searchUserField.getText();
        Map<String, Boolean> searchUsers = new HashMap<>();
        for (Map.Entry<String, Boolean> entry: lockedStatusOfUsers.entrySet()) {
            String username = entry.getKey();
            boolean isLocked = entry.getValue();
            if (username.contains(searchText)) {
                searchUsers.put(username, isLocked);
            }
        }
        loadUsers(searchUsers);
    }
    
    public static void open(Stage stage) {
        try {
            String filePath = Controller.DEFAULT_FXML_RESOURCE + "AdminDashboardView.fxml";
            AdminDashboardController controller = new AdminDashboardController();
            controller.setStage(stage);
            controller.loadFXMLAndSetScene(filePath, controller);
            controller.init();
            controller.showFXML();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open admin dashboard");
        }
    }
}
