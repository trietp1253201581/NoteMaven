package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.ShareNoteDAO;
import com.noteapp.dao.ShareNoteKey;
import com.noteapp.dao.UserKey;
import com.noteapp.model.dto.Note;
import com.noteapp.model.dto.ShareNote;
import java.util.List;
import com.noteapp.dao.BasicDAO;

/**
 * Xóa một Note đã có
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class DeleteNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDAO<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public DeleteNoteService() {
        noteId = -1;
    }

    public DeleteNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Override
    public Note execute() throws DAOException {    
        noteDataAccess = NoteDAO.getInstance();
        shareNoteDataAccess = ShareNoteDAO.getInstance();
        Note deleteNote = noteDataAccess.get(new NoteKey(noteId));
        if(deleteNote.getLastModified() == 1) {
            List<Note> remainNotes = noteDataAccess.getAll(new UserKey(deleteNote.getAuthor()));
            if(remainNotes.size() >= 2) {
                Note newLastNote = remainNotes.get(remainNotes.size()-2);
                newLastNote.setLastModified(1);
                noteDataAccess.update(newLastNote);
            }
        }
        shareNoteDataAccess.deleteAll(new ShareNoteKey(noteId, ""));
        noteDataAccess.delete(new NoteKey(noteId));
        return deleteNote;
    } 
}