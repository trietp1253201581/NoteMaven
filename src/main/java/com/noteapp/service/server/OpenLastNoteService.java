package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.UserKey;
import com.noteapp.model.datatransfer.Note;
import java.util.List;
import com.noteapp.dao.BasicDAO;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenLastNoteService implements ServerService<Note> {   
    private String author;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenLastNoteService() {
        author = "";
    }

    public OpenLastNoteService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Note execute() throws DAOException { 
        noteDataAccess = NoteDAO.getInstance();
        List<Note> notes = noteDataAccess.getAll(new UserKey(author));
        return notes.getLast();
    }    
}