package com.noteapp.service.server;

import com.noteapp.dao.DAO;
import com.noteapp.dao.DAOException;
import com.noteapp.dao.DAOKey;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.ShareNoteDAO;
import com.noteapp.model.Note;
import com.noteapp.model.NoteBlock;
import static com.noteapp.model.NoteBlock.BlockType.SURVEY;
import static com.noteapp.model.NoteBlock.BlockType.TEXT;
import com.noteapp.model.ShareNote;
import com.noteapp.model.SurveyBlock;
import com.noteapp.model.TextBlock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class ShareNoteService extends NoteService {
    protected DAO<ShareNote> shareNoteDAO;
    
    @Override
    protected void getInstanceOfDAO() {
        super.getInstanceOfDAO();
        shareNoteDAO = ShareNoteDAO.getInstance();
    }
    
    public ShareNote share(Note note, String editor, ShareNote.ShareType shareType) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(note.getId()));
        key.addKey("editor", editor);
        ShareNote shareNote = new ShareNote();
        shareNote.setEditor(editor);
        shareNote.setShareType(shareType);
        shareNote.setNote(note);
        try {
            shareNoteDAO.get(key);
            shareNoteDAO.update(shareNote);
            return this.open(note.getId(), editor);
        } catch (NotExistDataException ex1) {
        } catch (DAOException ex2) {
            throw new ServerServiceException(ex2.getMessage());
        }
        
        List<NoteBlock> authorBlocks = note.getBlocks();
        try {
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
            return this.open(shareNote.getId(), shareNote.getEditor());
        } catch (DAOException ex) {
            throw new ServerServiceException(ex.getMessage());
        }
    }
    
    public ShareNote open(int noteId, String editor) throws ServerServiceException {
        getInstanceOfDAO();
        Note note = super.open(noteId);
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(noteId));
        key.addKey("editor", editor);
        try {
            ShareNote shareNote = shareNoteDAO.get(key);
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
            throw new ServerServiceException(ex.getMessage());
        }
    }
    
    public List<ShareNote> getAllReceived(String editor) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        key.addKey("editor", editor);
        try {
            List<ShareNote> shareNotes = shareNoteDAO.getAll(key);
            List<ShareNote> receivedNotes = new ArrayList<>();
            for(ShareNote shareNote: shareNotes) {
                receivedNotes.add(this.open(shareNote.getId(), editor));
            }
            return receivedNotes;
        } catch (DAOException ex) {
            throw new ServerServiceException(ex.getMessage());
        }
    }
}
