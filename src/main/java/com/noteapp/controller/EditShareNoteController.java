package com.noteapp.controller;

import static com.noteapp.controller.Controller.showAlert;
import com.noteapp.note.model.NoteBlock;
import com.noteapp.note.model.ShareNote;
import com.noteapp.note.model.SurveyBlock;
import com.noteapp.note.model.TextBlock;
import com.noteapp.note.service.NoteServiceException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

/**
 *
 * @author admin
 */
public class EditShareNoteController extends EditNoteController {
    private Map<String, List<TextBlock>> otherTextBlockByHeaders;
    private Map<String, List<SurveyBlock>> otherSurveyBlockByHeaders;
    
    protected List<SurveyBlockController> surveyBlockControllers;
    protected ShareNote myShareNote;
    protected Timer updateTimer;
    protected TimerTask updateTimerTask;
    
    public void setMyShareNote(ShareNote myShareNote) {
        this.myShareNote = myShareNote;
    }
    
    @Override
    public void init() {
        surveyBlockControllers = new ArrayList<>();
        super.init();
        addSurveyBlockButton.setOnAction((ActionEvent event) -> {
            SurveyBlock newBlock = new SurveyBlock();
            newBlock.setOrder(myShareNote.getBlocks().size() + 1);
            newBlock.setHeader("SurveyBlock " + newBlock.getOrder() + " of " + myNote.getHeader());
            newBlock.setEditor(myUser.getUsername());
            addBlock(newBlock);
        });
    }
    
    @Override 
    protected void initView() {
        noteHeaderLabel.setText(myShareNote.getHeader());
        loadFilter(myNote.getFilters(), 8);
        this.initBlock();
    }
    
    @Override
    protected void initBlock() {
        blocksLayout.getChildren().clear();
        List<NoteBlock> blocks = myNote.getBlocks();
        Map<String, List<NoteBlock>> otherEditorBlocks = myShareNote.getOtherEditorBlocks();
        otherTextBlockByHeaders = new HashMap<>();
        otherSurveyBlockByHeaders = new HashMap<>();
        
        getBlocksByHeader(otherEditorBlocks);
        
        for(int i=0; i<blocks.size(); i++) {
            if(blocks.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                this.addBlock((TextBlock) blocks.get(i));
            } else {
                this.addBlock((SurveyBlock) blocks.get(i));
            }
        }
    }
    
    public void setOnAutoUpdate() {
        updateTimer = new Timer();
        updateTimerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        myShareNote = shareNoteService.open(myShareNote.getId(), myShareNote.getEditor());
                        noteHeaderLabel.setText(myShareNote.getHeader());
                        loadFilter(myShareNote.getFilters(), 8);
                        Map<String, List<NoteBlock>> otherEditorBlocks = myShareNote.getOtherEditorBlocks();
                        getBlocksByHeader(otherEditorBlocks);
                        updateTextBlock();
                        updateSurveyBlock();
                    } catch (NoteServiceException ex) {
                        showAlert(Alert.AlertType.WARNING, "Can't update!");
                    }
                });
            }
        };
        updateTimer.scheduleAtFixedRate(updateTimerTask, 2000, 4000);
    }
    
    @Override
    protected void addBlock(TextBlock newTextBlock) {
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "TextBlockView.fxml";
        try {
            String blockHeader = newTextBlock.getHeader();
            TextBlockController controller = new TextBlockController();
            
            VBox box = controller.loadFXML(filePath, controller);
            controller.init();
            controller.setTextBlock(newTextBlock);
            if (otherTextBlockByHeaders.containsKey(blockHeader)) {
                controller.setOtherEditors(otherTextBlockByHeaders.get(blockHeader));
            }
            controller.setNoteId(myNote.getId());
            controller.setTextForView(newTextBlock.getContent());
            controller.setHeader(newTextBlock.getHeader());
            controller.initOtherEditComboBox();
            
            controller.getDeleteButton().setOnAction((ActionEvent event) -> {
                int idxToDelete = controller.getTextBlock().getOrder()-1;
                blocksLayout.getChildren().remove(idxToDelete);
                myNote.getBlocks().remove(idxToDelete);
                textBlockControllers.remove(idxToDelete);
            });
            controller.getSwitchToOtherButton().setOnAction((ActionEvent event) -> {
                String otherEditor = controller.getOtherEditor();
                TextBlock otherTextBlock = new TextBlock();
                for (TextBlock block: otherTextBlockByHeaders.get(blockHeader)) {
                    if (block.getEditor().equals(otherEditor)) {
                        otherTextBlock = block;
                        break;
                    }
                }
                controller.setTextForView(otherTextBlock.getContent());
            });
             
            blocksLayout.getChildren().add(box);
            textBlockControllers.add(controller);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    @Override
    protected void saveMyNote() {
        myShareNote.setHeader(noteHeaderLabel.getText());
        myShareNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        myShareNote.getBlocks().clear();
        for(int i=0; i<textBlockControllers.size(); i++) {
            TextBlock block = textBlockControllers.get(i).getTextBlock();
            block.setContent(textBlockControllers.get(i).getTextFromView());
            myShareNote.getBlocks().add(block);
        }
        for(int i=0; i<surveyBlockControllers.size(); i++) {
            SurveyBlock block = surveyBlockControllers.get(i).getSurveyBlock();
            
            myShareNote.getBlocks().add(block);
        }
        try {
            noteService.save(myShareNote);
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myShareNote.getHeader());
        } catch (NoteServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void getBlocksByHeader(Map<String, List<NoteBlock>> otherEditorBlocks) {
        otherTextBlockByHeaders = new HashMap<>();
        otherSurveyBlockByHeaders = new HashMap<>();
        for(Map.Entry<String, List<NoteBlock>> entry: otherEditorBlocks.entrySet()) {
            String header = entry.getKey();
            List<NoteBlock> others = entry.getValue();
            
            for(int i = 0; i < others.size(); i++) {
                if(others.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                    if (otherEditorBlocks.containsKey(header)) {
                        otherTextBlockByHeaders.put(header, new ArrayList<>());
                    }
                    otherTextBlockByHeaders.get(header).add((TextBlock) others.get(i));
                } else {
                    if (otherEditorBlocks.containsKey(header)) {
                        otherSurveyBlockByHeaders.put(header, new ArrayList<>());
                    }
                    otherSurveyBlockByHeaders.get(header).add((SurveyBlock) others.get(i));
                }
            }
        }
        System.out.println(otherSurveyBlockByHeaders);
    }
    
    protected void updateTextBlock() {
        for (int i = 0; i < textBlockControllers.size(); i++) {
            TextBlock thisBlock = textBlockControllers.get(i).getTextBlock();
            List<TextBlock> otherEditors = otherTextBlockByHeaders.get(thisBlock.getHeader());

            textBlockControllers.get(i).updateOtherEditors(otherEditors);
            textBlockControllers.get(i).initOtherEditComboBox();
        }
    }
    
    protected void updateSurveyBlock() {
        for (int i = 0; i < surveyBlockControllers.size(); i++) {
            SurveyBlock thisBlock = surveyBlockControllers.get(i).getSurveyBlock();
            List<SurveyBlock> otherEditors = otherSurveyBlockByHeaders.get(thisBlock.getHeader());
            System.out.println("Hello" + i + " " + otherEditors);
            surveyBlockControllers.get(i).setOtherEditors(otherEditors);
            surveyBlockControllers.get(i).loadItems();
        }
    }
    
    protected void addBlock(SurveyBlock newSurveyBlock) {
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "SurveyBlockView.fxml";
        try {
            String blockHeader = newSurveyBlock.getHeader();
            SurveyBlockController controller = new SurveyBlockController();
            
            VBox box = controller.loadFXML(filePath, controller);
            controller.init();
            controller.setSurveyBlock(newSurveyBlock);
            if (otherSurveyBlockByHeaders.containsKey(blockHeader)) {
                controller.setOtherEditors(otherSurveyBlockByHeaders.get(blockHeader));
            }
            controller.setNoteId(myNote.getId());
            controller.setHeader(newSurveyBlock.getHeader());
            controller.loadItems();
            
            controller.getDeleteButton().setOnAction((ActionEvent event) -> {
                int idxToDelete = controller.getSurveyBlock().getOrder()-1;
                blocksLayout.getChildren().remove(idxToDelete);
                myNote.getBlocks().remove(idxToDelete);
                surveyBlockControllers.remove(idxToDelete);
            });
             
            blocksLayout.getChildren().add(box);
            surveyBlockControllers.add(controller);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
