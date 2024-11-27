package com.noteapp.controller;

import com.noteapp.model.NoteBlock;
import com.noteapp.model.ShareNote;
import com.noteapp.model.TextBlock;
import com.noteapp.service.server.ServerServiceException;
import java.io.IOException;
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
    
    protected ShareNote myShareNote;
    protected Timer updateTimer;
    protected TimerTask updateTimerTask;
    
    public void setMyShareNote(ShareNote myShareNote) {
        this.myShareNote = myShareNote;
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
        getTextBlocksByHeader(otherEditorBlocks);
        
        for(int i=0; i<blocks.size(); i++) {
            if(blocks.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                this.addBlock((TextBlock) blocks.get(i));
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
                        System.out.println(myShareNote);
                        Map<String, List<NoteBlock>> otherEditorBlocks = myShareNote.getOtherEditorBlocks();
                        getTextBlocksByHeader(otherEditorBlocks);
                        System.out.println(otherTextBlockByHeaders);
                        updateTextBlock();
                    } catch (ServerServiceException ex) {
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
            controller.setOtherEditors(otherTextBlockByHeaders.get(blockHeader));
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
    
    protected void getTextBlocksByHeader(Map<String, List<NoteBlock>> otherEditorBlocks) {
        otherTextBlockByHeaders = new HashMap<>();
        for(Map.Entry<String, List<NoteBlock>> entry: otherEditorBlocks.entrySet()) {
            String header = entry.getKey();
            List<NoteBlock> others = entry.getValue();
            otherTextBlockByHeaders.put(header, new ArrayList<>());
            for(int i = 0; i < others.size(); i++) {
                if(others.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                    otherTextBlockByHeaders.get(header).add((TextBlock) others.get(i));
                }
            }
        }
    }
    
    protected void updateTextBlock() {
        System.out.println(textBlockControllers.size());
        for (int i = 0; i < textBlockControllers.size(); i++) {
            TextBlock thisBlock = textBlockControllers.get(i).getTextBlock();
            List<TextBlock> otherEditors = otherTextBlockByHeaders.get(thisBlock.getHeader());
            System.out.println(thisBlock + ":" + otherEditors);
            textBlockControllers.get(i).updateOtherEditors(otherEditors);
            textBlockControllers.get(i).initOtherEditComboBox();
        }
    }
}
