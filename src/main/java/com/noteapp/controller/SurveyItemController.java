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
        num.setText("0");
        other.setText("");
    }
    
    public void setChoice(String choice) {
        this.choice.setText(choice);
    }
    
    public String getChoice() {
        return choice.getText();
    }
    
    public int getNum() {
        return Integer.parseInt(num.getText());
    }

    public void setNum(int num) {
        this.num.setText(String.valueOf(num));
    }
    
    public void setOther(List<String> others) {
        if (others.isEmpty()) {
            return;
        }
        String otherStr = "";
        for (String otherVoted: others) {
            otherStr += otherVoted + ", ";
        }
        other.setText(otherStr.substring(0, otherStr.length() - 2));
    }
    
    public boolean getVoted() {
        return voted.isSelected();
    }
    
    public void setVoted(boolean isVoted) {
        voted.setSelected(isVoted);
        int nowNum = getNum();
        if (isVoted) {
            nowNum++;
        } else {
            nowNum--;
        }
        setNum(nowNum);
    }
    
    public Button getDeleteChoiceButton() {
        return deleteChoiceButton;
    }
    
    public RadioButton getVotedRatioButton() {
        return voted;
    }
}
