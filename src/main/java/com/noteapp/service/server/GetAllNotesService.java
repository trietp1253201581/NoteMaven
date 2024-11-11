package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.UserKey;
import com.noteapp.model.datatransfer.Note;

import java.util.List;
import com.noteapp.dao.BasicDAO;

/**
 * Lấy tất cả các note của một user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class GetAllNotesService implements CollectionServerService<Note> {   
    private String author;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    
    public GetAllNotesService() {
        author = "";
    }
    
    public GetAllNotesService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
  
    @Override
    public List<Note> execute() throws DAOException {       
        noteDataAccess = NoteDAO.getInstance();
        return noteDataAccess.getAll(new UserKey(author));
    }   
}