/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.controller;

import com.noteapp.model.SurveyBlock;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author admin
 */
public class SurveyBlockController extends Controller {
    @FXML
    private Label blockHeader;
    @FXML
    private Label changeNotify;
    @FXML
    private VBox itemsLayout;
    @FXML
    private Button addItemButton;
    
    private int noteId;
    private SurveyBlock surveyBlock;
    private List<SurveyBlock> otherEditors;
    
    @Override
    public void init() {
        
    }
    
    
    
    public String getHeader() {
        return blockHeader.getText();
    }
    
    public void setHeader(String header) {
        blockHeader.setText(header);
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public SurveyBlock getSurveyBlock() {
        return surveyBlock;
    }

    public void setSurveyBlock(SurveyBlock surveyBlock) {
        this.surveyBlock = surveyBlock;
    }

    public List<SurveyBlock> getOtherEditors() {
        return otherEditors;
    }

    public void setOtherEditors(List<SurveyBlock> otherEditors) {
        this.otherEditors = otherEditors;
    }
    
    
}
