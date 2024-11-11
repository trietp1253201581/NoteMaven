package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.FailedExecuteException;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.NullKey;
import com.noteapp.dao.UserDAO;
import com.noteapp.dao.UserKey;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.User;
import java.util.List;
import com.noteapp.dao.BasicDAO;

/**
 * Tạo một Note mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateNoteService implements ServerService<Note> {   
    private Note note;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDAO<User, UserKey, NullKey> userDataAccess;

    public CreateNoteService() {
        note = new Note();
    }

    public CreateNoteService(Note note) {
        this.note = note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
    
    @Override
    public Note execute() throws DAOException {
        noteDataAccess = NoteDAO.getInstance();  
        userDataAccess = UserDAO.getInstance();
        try {
            noteDataAccess.get(new NoteKey(note.getId()));
            throw new DAOException("This note already exists");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(note.getAuthor()));
            List<Note> existNotes = noteDataAccess.getAll();
            Note lastNote = existNotes.getLast();
            note.setId(lastNote.getId() + 1);
            return noteDataAccess.add(note);         
        }
    }    
}