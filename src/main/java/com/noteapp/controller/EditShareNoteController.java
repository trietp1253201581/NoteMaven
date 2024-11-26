package com.noteapp.controller;

import com.noteapp.model.NoteBlock;
import com.noteapp.model.ShareNote;
import com.noteapp.model.TextBlock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author admin
 */
public class EditShareNoteController extends EditNoteController {
    private Map<String, List<TextBlock>> otherTextBlockByHeaders;
    
    protected ShareNote myShareNote;
    
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
        System.out.println(myShareNote);
        for(int i=0; i<blocks.size(); i++) {
            if(blocks.get(i).getBlockType() == NoteBlock.BlockType.TEXT) {
                this.addBlock((TextBlock) blocks.get(i));
            }
        }
    }
    
    @Override
    protected void addBlock(TextBlock newTextBlock) {
        String filePath = Controller.DEFAULT_FXML_RESOURCE + "TextBlockView.fxml";
        try {
            String blockHeader = newTextBlock.getHeader();
            TextBlockController controller = new TextBlockController();
            controller.setTextBlock(newTextBlock);
            controller.setOtherEditors(otherTextBlockByHeaders.get(blockHeader));
            controller.setNoteId(myNote.getId());
            
            
            VBox box = controller.loadFXML(filePath, controller);
            controller.init();
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
}
