package com.noteapp.controller;

import com.noteapp.note.model.Note;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 *
 * @author admin
 */
public class RecentlyNoteCardController extends Controller {
    @FXML
    private Text header;
    @FXML
    private Text lastModifiedDate;
    @FXML
    private Text editor;
    
    private Note note;
    
    public void setData(Note note) {
        this.note = note;
        header.setText(note.getHeader());
        lastModifiedDate.setText(note.getLastModifiedDate().toString());
        editor.setText(note.getAuthor());
    }
    
    public Note getNote() {
        return note;
    }
}