package com.noteapp.service.server;

import com.noteapp.dao.DAO;
import com.noteapp.dao.DAOException;
import com.noteapp.dao.DAOKey;
import com.noteapp.dao.NotExistDataException;
import com.noteapp.dao.NoteBlockDAO;
import com.noteapp.dao.NoteDAO;
import com.noteapp.dao.NoteFilterDAO;
import com.noteapp.dao.SurveyBlockDAO;
import com.noteapp.dao.TextBlockDAO;
import com.noteapp.model.Note;
import com.noteapp.model.NoteBlock;
import com.noteapp.model.NoteFilter;
import com.noteapp.model.SurveyBlock;
import com.noteapp.model.TextBlock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class NoteService {
    protected DAO<Note> noteDAO;
    protected DAO<NoteFilter> noteFilterDAO;
    protected DAO<NoteBlock> noteBlockDAO;
    protected DAO<TextBlock> textBlockDAO;
    protected DAO<SurveyBlock> surveyBlockDAO;

    public NoteService() {

    }
    
    protected void getInstanceOfDAO() {
        noteDAO = NoteDAO.getInstance();
        noteFilterDAO = NoteFilterDAO.getInstance();
        noteBlockDAO = NoteBlockDAO.getInstance();
        textBlockDAO = TextBlockDAO.getInstance();
        surveyBlockDAO = SurveyBlockDAO.getInstance();
    }
    
    public Note create(Note newNote) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "note_id";
        key.addKey(keyName, String.valueOf(newNote.getId()));
        try {          
            noteDAO.get(key);
            throw new ServerServiceException("Already exist note!");
        } catch (NotExistDataException nedExByGet) {
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
        try {
            //Note
            newNote = noteDAO.create(newNote);
            key.addKey("note_id", String.valueOf(newNote.getId()));
            //Filter
            for(NoteFilter newNoteFilter: newNote.getFilters()) {
                noteFilterDAO.create(newNoteFilter, key);
            }
            //NoteBlock
            for(NoteBlock newNoteBlock: newNote.getBlocks()) {
                newNoteBlock.setEditor(newNote.getAuthor());
                createBlock(newNote.getId(), newNoteBlock);
            }
            return this.open(newNote.getId());
        } catch (DAOException exByCreate) {
            throw new ServerServiceException(exByCreate.getMessage());
        }
    }
    
    public Note delete(int noteId) throws ServerServiceException {
        getInstanceOfDAO();
        try {
            DAOKey key = new DAOKey();
            String keyName = "note_id";
            key.addKey(keyName, String.valueOf(noteId));
            Note deletedNote = this.open(noteId);
            noteDAO.delete(key);
            return deletedNote;
        } catch (DAOException exByGetAndDelete) {
            throw new ServerServiceException(exByGetAndDelete.getMessage());
        }
    }
    
    public List<Note> getAll(String author) throws ServerServiceException {
        getInstanceOfDAO();
        try {
            DAOKey key = new DAOKey();
            String keyName = "author";
            key.addKey(keyName, author);
            List<Note> notes = noteDAO.getAll(key);
            List<Note> returnNotes = new ArrayList<>();
            for(Note note: notes) {
                returnNotes.add(this.open(note.getId()));
            }
            return returnNotes;
        } catch (DAOException exByGetAll) {
            throw new ServerServiceException(exByGetAll.getMessage());
        }
    }
    
    public Note open(int noteId) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "note_id";
        key.addKey(keyName, String.valueOf(noteId));
        try {
            Note note = noteDAO.get(key);
            note.setFilters(noteFilterDAO.getAll(key));
            List<NoteBlock> noteBlocks = this.getAllBlocks(noteId, note.getAuthor());
            note.setBlocks(noteBlocks);
            return note;
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
    } 
    
    public Note save(Note note) throws ServerServiceException {
        getInstanceOfDAO();
        DAOKey key = new DAOKey();
        String keyName = "note_id";
        key.addKey(keyName, String.valueOf(note.getId()));
        try {
            noteDAO.get(key);
        } catch (NotExistDataException nedExByGet) {
            return this.create(note);
        } catch (DAOException exByGet) {
            throw new ServerServiceException(exByGet.getMessage());
        }
        try {
            noteDAO.update(note);
            noteFilterDAO.deleteAll(key);
            //Filter
            for(NoteFilter noteFilter: note.getFilters()) {
                noteFilterDAO.create(noteFilter, key);
            }
            saveBlocks(note.getId(), note.getBlocks());
            return note;
        } catch (DAOException exByUpdate) {
            throw new ServerServiceException(exByUpdate.getMessage());
        }
    }
    
    protected List<NoteBlock> getAllBlocks(int noteId) throws DAOException {
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(noteId));
        List<NoteBlock> noteBlocks = noteBlockDAO.getAll(key);
        List<NoteBlock> returnBlocks = new ArrayList<>();
        for(NoteBlock noteBlock: noteBlocks) {
            key.addKey("block_id", String.valueOf(noteBlock.getId()));
            switch (noteBlock.getBlockType()) {
                case TEXT -> {
                    List<TextBlock> textBlocks = textBlockDAO.getAll(key);
                    for(TextBlock textBlock: textBlocks) {
                        textBlock.setNoteBlock(noteBlock);
                        returnBlocks.add(textBlock);
                    }
                }

                case SURVEY -> {
                    List<SurveyBlock> surveyBlocks = surveyBlockDAO.getAll(key);
                    for(SurveyBlock surveyBlock: surveyBlocks) {
                        surveyBlock.setNoteBlock(noteBlock);
                        returnBlocks.add(surveyBlock);
                    }
                }
            }
        }
        return returnBlocks;
    }
        
    
    protected List<NoteBlock> getAllBlocks(int noteId, String editor) throws DAOException {
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(noteId));
        List<NoteBlock> noteBlocks = noteBlockDAO.getAll(key);
        List<NoteBlock> returnBlocks = new ArrayList<>();
        for(NoteBlock noteBlock: noteBlocks) {
            key.addKey("block_id", String.valueOf(noteBlock.getId()));
            switch (noteBlock.getBlockType()) {
                case TEXT -> {
                    List<TextBlock> textBlocks = textBlockDAO.getAll(key);
                    for(TextBlock textBlock: textBlocks) {
                        if(editor.equals(textBlock.getEditor())) {
                            textBlock.setNoteBlock(noteBlock);
                            returnBlocks.add(textBlock);
                        }
                    }
                }

                case SURVEY -> {
                    List<SurveyBlock> surveyBlocks = surveyBlockDAO.getAll(key);
                    for(SurveyBlock surveyBlock: surveyBlocks) {
                        if(editor.equals(surveyBlock.getEditor())) {
                            surveyBlock.setNoteBlock(noteBlock);
                            returnBlocks.add(surveyBlock);
                        }
                    }
                }
            }
        }
        return returnBlocks;
    }
    
    protected void createBlock(int noteId, NoteBlock newBlock) throws DAOException {
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(noteId));
        try {
            key.addKey("block_id", String.valueOf(newBlock.getId()));
            noteBlockDAO.get(key);
        } catch (NotExistDataException ex1) {
        } catch (DAOException ex2) {
            throw ex2;
        }
        newBlock = noteBlockDAO.create(newBlock, key);
        switch (newBlock.getBlockType()) {
            case TEXT -> {
                TextBlock newTextBlock = (TextBlock) newBlock;
                textBlockDAO.create(newTextBlock);
            }
            case SURVEY -> {
                SurveyBlock newSurveyBlock = (SurveyBlock) newBlock;
                surveyBlockDAO.create(newSurveyBlock);
            }

        }
    }
    
    protected void updateBlock(int noteId, NoteBlock needUpdateBlock) throws DAOException {
        DAOKey key = new DAOKey();
        key.addKey("note_id", String.valueOf(noteId));
        noteBlockDAO.update(needUpdateBlock, key);
        switch (needUpdateBlock.getBlockType()) {
            case TEXT -> {
                TextBlock needUpdateTextBlock = (TextBlock) needUpdateBlock;
                textBlockDAO.update(needUpdateTextBlock);
            }
            case SURVEY -> {
                SurveyBlock needUpdateSurveyBlock = (SurveyBlock) needUpdateBlock;
                surveyBlockDAO.update(needUpdateSurveyBlock);
            }
        }
    }
    
    protected void deleteBlock(int blockId) throws DAOException {
        DAOKey key = new DAOKey();
        key.addKey("block_id", String.valueOf(blockId));
        noteBlockDAO.delete(key);
    }
    
    protected void saveBlocks(int noteId, List<NoteBlock> noteBlocks) throws DAOException {
        String editor = noteBlocks.get(0).getEditor();
        List<NoteBlock> blocksInDB = this.getAllBlocks(noteId, editor);
        List<NoteBlock> newBlocks = new ArrayList<>();
        List<NoteBlock> needUpdateBlocks = new ArrayList<>();
        List<NoteBlock> deletedBlocks = new ArrayList<>();
        for(NoteBlock noteBlock: noteBlocks) {
            if(blocksInDB.contains(noteBlock)) {
                needUpdateBlocks.add(noteBlock);
            } else {
                newBlocks.add(noteBlock);
            }
        }
        for(NoteBlock noteBlock: blocksInDB) {
            if(!noteBlocks.contains(noteBlock)) {
                deletedBlocks.add(noteBlock);
            }
        }
        for(NoteBlock newBlock: newBlocks) {
            this.createBlock(noteId, newBlock);
        }
        for(NoteBlock needUpdateBlock: needUpdateBlocks) {
            this.updateBlock(noteId, needUpdateBlock);
        }
        for(NoteBlock deletedBlock: deletedBlocks) {
            this.deleteBlock(deletedBlock.getId());
        }
    }
}