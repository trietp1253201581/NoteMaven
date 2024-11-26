package com.noteapp.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 *
 * @author admin
 */
public class SurveyItemController extends Controller {
    @FXML
    private Label choice;
    @FXML
    private RadioButton voted;
    @FXML
    private Button deleteChoiceButton;
    @FXML
    private Label num;
    @FXML
    private Label other;
    
    @Override 
    public void init() {
        voted.setSelected(false);
    }
    
    public void setChoice(String choice) {
        this.choice.setText(choice);
    }

    public void setNum(int num) {
        this.num.setText(String.valueOf(num));
    }
    
    public void setOther(List<String> others) {
        String otherStr = "";
        for (String otherVoted: others) {
            otherStr += otherVoted + ", ";
        }
        other.setText(otherStr.substring(0, others.size() - 2));
    }
    
    public boolean getVoted() {
        return voted.isSelected();
    }
    
    public void vote() {
        voted.setSelected(true);
    }
    
    public void unvote() {
        voted.setSelected(false);
    }
    
    public Button getDeleteChoiceButton() {
        return deleteChoiceButton;
    }
}
