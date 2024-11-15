package com.noteapp;


import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class chính của project, tạo ứng dụng chạy cho user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLite extends Application {
    private double posX, posY;
    
    /**
     * @param primaryStage Stage khởi tạo
     */
    @Override
    public void start(Stage primaryStage) {
        System.out.println("hello");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
}