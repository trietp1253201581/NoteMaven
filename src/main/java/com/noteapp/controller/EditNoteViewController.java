package com.noteapp.controller;

import com.noteapp.dao.DAOException;
import com.noteapp.model.Note;
import com.noteapp.model.NoteBlock;
import com.noteapp.model.NoteFilter;
import com.noteapp.model.TextBlock;
import com.noteapp.model.User;
import com.noteapp.service.server.ServerServiceException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * FXML Controller class
 *
 * @author admin
 */
public class EditNoteViewController extends Controller {
    //Các thuộc tính chung
    @FXML 
    private Label noteHeaderLabel;
    @FXML
    private Button homeMenuButton;
    @FXML
    private Button editMenuButton;
    @FXML
    private HBox editBox;
    //Các thuộc tính của edit Box
    @FXML
    private Button saveNoteButton;
    @FXML
    private Button openNoteButton;
    @FXML 
    private Button addFilterButton;
    @FXML
    private Button addTextBlockButton;
    @FXML
    private ComboBox<String> fontTypeComboBox; 
    @FXML
    private ComboBox<String> fontSizeComboBox;
    @FXML
    private ColorPicker colorPicker;
    //Các thuộc tính còn lại
    @FXML
    private VBox blocksLayout;
    @FXML
    private GridPane filterGridLayout;
    @FXML
    private Button closeButton;
    
    private User myUser;
    private Note myNote;
    private List<TextBlockController> myNoteBlockControllers;

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
    
    public void setMyNote(Note myNote) {
        this.myNote = myNote;
    }
    
    @Override
    public void init() {
        initServerService();
        myNoteBlockControllers = new ArrayList<>();
        initView();
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        homeMenuButton.setOnAction((ActionEvent event) -> {
            openDashboard(myUser);
        });
        noteHeaderLabel.setOnMouseClicked((MouseEvent event) -> {
            changeHeaderLabel();
        });
        saveNoteButton.setOnAction((ActionEvent event) -> {
            saveMyNote();
        });
        addFilterButton.setOnAction((ActionEvent event) -> {
            addFilter();
        });
        addTextBlockButton.setOnAction((ActionEvent event) -> {
            TextBlock newBlock = new TextBlock();
            newBlock.setOrder(myNoteBlockControllers.size() + 1);
            newBlock.setHeader("Block " + newBlock.getOrder() + " of " + myNote.getHeader());
            newBlock.setEditor(myUser.getUsername());
            newBlock.setContent("Edit here");
            addBlock(newBlock);
        });
    }
    
    protected void initView() {       
        noteHeaderLabel.setText(myNote.getHeader());
        loadFilter(myNote.getFilters(), 8);
        blocksLayout.getChildren().clear();
        List<NoteBlock> blocks = myNote.getBlocks();
        for(int i=0; i<blocks.size(); i++) {
            if(blocks.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                addBlock((TextBlock) blocks.get(i));
            }
        }
    }
    
    @Override
    protected void close() {
        //saveMyNote();
        super.close();
    }
    
    protected void changeHeaderLabel() {
        //Mở dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Change header for " + myNote.getHeader());
        inputDialog.setHeaderText("Input your new header");
        //Lấy kết quả và xử lý
        Optional<String> confirm = inputDialog.showAndWait();
        confirm.ifPresent(newNoteHeader -> {
            //Lấy tất cả các Note của myUser
            try { 
                List<Note> myNotes = noteService.getAll(myUser.getUsername());
                for(Note note: myNotes) {
                    if(note.getHeader().equals(newNoteHeader)) {
                        showAlert(Alert.AlertType.ERROR, "This header exist");
                        return;
                    }
                }
                //Thiết lập note name vừa nhập cho Label   
                noteHeaderLabel.setText(newNoteHeader);
            } catch (ServerServiceException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
    }
    
    protected void saveMyNote() {
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        myNote.getBlocks().clear();
        for(int i=0; i<myNoteBlockControllers.size(); i++) {
            TextBlock block = myNoteBlockControllers.get(i).getTextBlock();
            block.setContent(myNoteBlockControllers.get(i).getTextFromView());
            block.setOrder(i+1);
            myNote.getBlocks().add(block);
        }
        try {
            noteService.save(myNote);
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
        } catch (ServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void addBlock(TextBlock newTextBlock) {
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "TextBlockView.fxml";
        try {
            TextBlockController controller = new TextBlockController();
            controller.setTextBlock(newTextBlock);
            
            controller.setNoteId(myNote.getId());
            
            
            VBox box = controller.loadFXML(filePath, controller);
            controller.init();
            controller.setTextForView(newTextBlock.getContent());
            controller.setHeader(newTextBlock.getHeader());
            
            controller.getDeleteButton().setOnAction((ActionEvent event) -> {
                int idxToDelete = controller.getTextBlock().getOrder()-1;
                blocksLayout.getChildren().remove(idxToDelete);
                myNote.getBlocks().remove(idxToDelete);
                myNoteBlockControllers.remove(idxToDelete);
            });
             
            blocksLayout.getChildren().add(box);
            myNoteBlockControllers.add(controller);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    protected void addFilter() {
        //Hiện Dialog để nhập filter mới
        TextInputDialog inputDialog = new TextInputDialog();        
        inputDialog.setTitle("Add filter for " + myNote.getHeader());
        inputDialog.setHeaderText("Enter your new filter");   
        //Lấy kết quả
        Optional<String> confirm = inputDialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(newFilterStr -> {
            NoteFilter newFilter = new NoteFilter(newFilterStr);
            //Thiết lập và add tất cả các filter cũ     
            if(myNote.getFilters().contains(newFilter)) {
                //Nếu filter đã tồn tại thì thông báo lỗi
                showAlert(Alert.AlertType.ERROR, "Exist Filter");
            } else {
                //Thêm filter vào list
                myNote.getFilters().add(newFilter);
                //Load lại filter GUI
                loadFilter(myNote.getFilters(), 8);
            }
        }); 
    }
    
    protected void loadFilter(List<NoteFilter> filters, int maxColEachRow) {
        int column = 0;
        int row = 0;
        //Làm sạch filter layout
        filterGridLayout.getChildren().clear();
        if(filters.isEmpty()) {
            return;
        }
        //Thiết lập khoảng cách giữa các filter
        filterGridLayout.setHgap(8);
        filterGridLayout.setVgap(8);
        //Thiết lập filter layout
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "NoteFiltersView.fxml";
        try {
            for(int i = 0; i < filters.size(); i++) {
                NoteFiltersController controller = new NoteFiltersController();
                HBox hbox = controller.loadFXML(filePath, controller);
                //Thiết lập dữ liệu cho filter
                controller.setData(filters.get(i).getFilterContent());
                controller.getRemoveFilterView().setOnMouseClicked(event -> {
                    myNote.getFilters().remove(new NoteFilter(controller.getFilter()));
                    loadFilter(myNote.getFilters(), 8);
                });
                //Chuyển hàng
                if(column == maxColEachRow){
                    column = 0;
                    row++;
                }
                //Thêm filter vừa tạo vào layout
                filterGridLayout.add(hbox, column++, row);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    protected void openDashboard(User user) {
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
