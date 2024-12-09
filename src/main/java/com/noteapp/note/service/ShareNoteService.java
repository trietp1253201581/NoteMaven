package com.noteapp.note.service;

import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.note.dao.IShareNoteDAO;
import com.noteapp.note.dao.ShareNoteDAO;
import com.noteapp.note.model.Note;
import com.noteapp.note.model.NoteBlock;
import static com.noteapp.note.model.NoteBlock.BlockType.SURVEY;
import static com.noteapp.note.model.NoteBlock.BlockType.TEXT;
import com.noteapp.note.model.ShareNote;
import com.noteapp.note.model.SurveyBlock;
import com.noteapp.note.model.TextBlock;
import com.noteapp.user.dao.IUserDAO;
import com.noteapp.user.dao.UserDAO;
import com.noteapp.user.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class ShareNoteService extends NoteService {
    protected IShareNoteDAO shareNoteDAO;
    protected IUserDAO userDAO;
    
    @Override
    protected void getInstanceOfDAO() {
        super.getInstanceOfDAO();
        shareNoteDAO = ShareNoteDAO.getInstance();
        userDAO = UserDAO.getInstance();
    }
    
    public ShareNote share(Note note, String editor, ShareNote.ShareType shareType) throws NoteServiceException {
        getInstanceOfDAO();
        try {
            userDAO.get(editor);
        } catch (DAOException ex) {
            ex.printStackTrace();
            throw new NoteServiceException(ex.getMessage());
        } 
        int noteId = note.getId();
      
        ShareNote shareNote = new ShareNote();
        
        shareNote.setNote(note);
        
        try {
            shareNoteDAO.get(noteId, editor);
            shareNoteDAO.update(shareNote);
            return this.open(note.getId(), editor);
        } catch (NotExistDataException ex1) {
        } catch (DAOException ex2) {
            ex2.printStackTrace();
            throw new NoteServiceException(ex2.getMessage());
        }
        
        List<NoteBlock> authorBlocks = note.getBlocks();
        try {
            shareNote.setShareType(ShareNote.ShareType.CAN_EDIT);
            shareNote.setEditor(note.getAuthor());
            shareNoteDAO.create(shareNote);
            shareNote.setEditor(editor);
            shareNote.setShareType(shareType);
            for(NoteBlock noteBlock: authorBlocks) {
                NoteBlock thisEditorBlock = noteBlock;
                thisEditorBlock.setEditor(shareNote.getEditor());
                switch (thisEditorBlock.getBlockType()) {
                    case TEXT -> {
                        TextBlock newTextBlock = (TextBlock) thisEditorBlock;
                        textBlockDAO.create(newTextBlock);
                    }
                    case SURVEY -> {
                        SurveyBlock newSurveyBlock = (SurveyBlock) thisEditorBlock;
                        surveyBlockDAO.create(newSurveyBlock);
                    }

                }
            }
            shareNoteDAO.create(shareNote);
            note.setPubliced(true);
            super.save(note);
            return this.open(shareNote.getId(), shareNote.getEditor());
        } catch (DAOException ex) {
            ex.printStackTrace();
            throw new NoteServiceException(ex.getMessage());
        }
    }
    
    public ShareNote open(int noteId, String editor) throws NoteServiceException {
        getInstanceOfDAO();
        Note note = super.open(noteId);

        try {
            ShareNote shareNote = shareNoteDAO.get(noteId, editor);
            shareNote.setNote(note);
            List<NoteBlock> blocks = super.getAllBlocks(noteId);
            List<NoteBlock> thisEditorBlocks = new ArrayList<>();
            for(NoteBlock noteBlock: blocks) {
                if(editor.equals(noteBlock.getEditor())) {
                    thisEditorBlocks.add(noteBlock);
                } else {
                    shareNote.addOtherEditorBlock(noteBlock);
                }
            }
            shareNote.setBlocks(thisEditorBlocks);
            return shareNote;
        } catch (DAOException ex) {
            ex.printStackTrace();
            throw new NoteServiceException(ex.getMessage());
        }
    }
    
    public List<ShareNote> getAllReceived(String editor) throws NoteServiceException {
        getInstanceOfDAO();
        try {
            List<ShareNote> shareNotes = shareNoteDAO.getAll(editor);
            List<ShareNote> receivedNotes = new ArrayList<>();
            for(ShareNote shareNote: shareNotes) {
                receivedNotes.add(this.open(shareNote.getId(), editor));
            }
            return receivedNotes;
        } catch (DAOException ex) {
            throw new NoteServiceException(ex.getMessage());
        }
    }
}
