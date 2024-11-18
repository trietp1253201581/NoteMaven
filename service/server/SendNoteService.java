package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.FailedExecuteException;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteKey;
import com.noteapp.dao.NullKey;
import com.noteapp.dao.ShareNoteDAO;
import com.noteapp.dao.ShareNoteKey;
import com.noteapp.dao.UserDAO;
import com.noteapp.dao.UserKey;
import com.noteapp.model.dto.Note;
import com.noteapp.model.dto.ShareNote;
import com.noteapp.model.dto.User;
import com.noteapp.dao.BasicDAO;


/**
 * Send một Note tới user khác
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class SendNoteService implements ServerService<ShareNote> {
    private ShareNote shareNote;
    protected BasicDAO<User, UserKey, NullKey> userDataAccess;
    protected BasicDAO<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDAO<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public SendNoteService() {
        shareNote = new ShareNote();
    }

    public SendNoteService(ShareNote shareNote) {
        this.shareNote = shareNote;
    }

    public void setShareNote(ShareNote shareNote) {
        this.shareNote = shareNote;
    }
        @Override
    public ShareNote execute() throws DAOException {
        userDataAccess = UserDAO.getInstance();
        noteDataAccess = NoteDAO.getInstance();
        shareNoteDataAccess = ShareNoteDAO.getInstance();
        try {
            shareNoteDataAccess.get(new ShareNoteKey(shareNote.getId(), shareNote.getReceiver()));
            shareNoteDataAccess.update(shareNote);
            return shareNote;
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(shareNote.getReceiver()));
            noteDataAccess.get(new NoteKey(shareNote.getId()));
            return shareNoteDataAccess.add(shareNote);
        }
    }
}