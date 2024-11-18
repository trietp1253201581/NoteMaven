package com.noteapp.controller;

import com.noteapp.model.dto.Note;
import com.noteapp.model.dto.NoteBlock;
import com.noteapp.model.dto.ShareNote;
import com.noteapp.model.dto.User;
import com.noteapp.service.security.MailService;
import com.noteapp.service.security.VerificationCodeService;
import com.noteapp.service.server.CollectionServerService;
import com.noteapp.service.server.ServerService;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public abstract class Controller {
    protected double posX, posY;
    protected ServerService<User> userService;
    protected ServerService<Note> noteService;
    protected ServerService<ShareNote> shareNoteService;
    protected ServerService<NoteBlock> noteBlockService;
    protected CollectionServerService<Note> noteCollectionService;
    protected CollectionServerService<ShareNote> shareNoteCollectionService;
    protected MailService mailService;
    protected VerificationCodeService verificationCodeService;
    protected Stage stage;
    protected Scene scene;
    
    public abstract void init();
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
    
    public void setSceneMoveable() {
        posX = 0;
        posY = 0;
        scene.setOnMousePressed((MouseEvent mouseEvent) -> {
            posX = mouseEvent.getSceneX();
            posY = mouseEvent.getSceneY();
        });       
        scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
            stage.setX(mouseEvent.getScreenX() - posX);
            stage.setY(mouseEvent.getScreenY() - posY);
        });
    }
    
    public void setResource(URL fXMLLocation) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fXMLLocation);
        scene = new Scene(loader.load());
    }
           
    protected void close() {
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
