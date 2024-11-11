package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.UserKey;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.dao.BasicDAO;

/**
 * Mở note với header và author cho trước
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenNoteService() {
        noteId = -1;
    }

    public OpenNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
 
    @Override
    public Note execute() throws DAOException {        
        noteDataAccess = NoteDAO.getInstance();
        return noteDataAccess.get(new NoteKey(noteId));
    }    
}