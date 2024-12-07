package com.noteapp.note.service;

import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.note.dao.INoteBlockDAO;
import com.noteapp.note.dao.INoteDAO;
import com.noteapp.note.dao.INoteFilterDAO;
import com.noteapp.note.dao.ISurveyBlockDAO;
import com.noteapp.note.dao.ITextBlockDAO;
import com.noteapp.note.dao.NoteBlockDAO;
import com.noteapp.note.dao.NoteDAO;
import com.noteapp.note.dao.NoteFilterDAO;
import com.noteapp.note.dao.SurveyBlockDAO;
import com.noteapp.note.dao.TextBlockDAO;
import com.noteapp.note.model.Note;
import com.noteapp.note.model.NoteBlock;
import com.noteapp.note.model.NoteFilter;
import com.noteapp.note.model.SurveyBlock;
import com.noteapp.note.model.TextBlock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class NoteService {
    protected INoteDAO noteDAO;
    protected INoteFilterDAO noteFilterDAO;
    protected INoteBlockDAO noteBlockDAO;
    protected ITextBlockDAO textBlockDAO;
    protected ISurveyBlockDAO surveyBlockDAO;

    public NoteService() {
        
    }
    
    protected void getInstanceOfDAO() {
        noteDAO = NoteDAO.getInstance();
        noteFilterDAO = NoteFilterDAO.getInstance();
        noteBlockDAO = NoteBlockDAO.getInstance();
        textBlockDAO = TextBlockDAO.getInstance();
        surveyBlockDAO = SurveyBlockDAO.getInstance();
    }
    
    public Note create(Note newNote) throws NoteServiceException {
        getInstanceOfDAO();
        int noteId = newNote.getId();
        try {          
            noteDAO.get(noteId);
            throw new NoteServiceException("Already exist note!");
        } catch (NotExistDataException nedExByGet) {
        } catch (DAOException exByGet) {
            throw new NoteServiceException(exByGet.getMessage());
        }
        try {
            //Note
            newNote = noteDAO.create(newNote);
            //Filter
            for(NoteFilter newNoteFilter: newNote.getFilters()) {
                noteFilterDAO.create(noteId, newNoteFilter);
            }
            //NoteBlock
            for(NoteBlock newNoteBlock: newNote.getBlocks()) {
                newNoteBlock.setEditor(newNote.getAuthor());
                createBlock(newNote.getId(), newNoteBlock);
            }
            return this.open(newNote.getId());
        } catch (DAOException exByCreate) {
            throw new NoteServiceException(exByCreate.getMessage());
        }
    }
    
    public Note delete(int noteId) throws NoteServiceException {
        getInstanceOfDAO();
        try {
            Note deletedNote = this.open(noteId);
            noteDAO.delete(noteId);
            return deletedNote;
        } catch (DAOException exByGetAndDelete) {
            throw new NoteServiceException(exByGetAndDelete.getMessage());
        }
    }
    
    public List<Note> getAll(String author) throws NoteServiceException {
        getInstanceOfDAO();
        try {
            List<Note> notes = noteDAO.getAll(author);
            List<Note> returnNotes = new ArrayList<>();
            for(Note note: notes) {
                returnNotes.add(this.open(note.getId()));
            }
            return returnNotes;
        } catch (DAOException exByGetAll) {
            throw new NoteServiceException(exByGetAll.getMessage());
        }
    }
    
    public Note open(int noteId) throws NoteServiceException {
        getInstanceOfDAO();
        try {
            Note note = noteDAO.get(noteId);
            note.setFilters(noteFilterDAO.getAll(noteId));
            List<NoteBlock> noteBlocks = this.getAllBlocks(noteId, note.getAuthor());
            note.setBlocks(noteBlocks);
            return note;
        } catch (DAOException exByGet) {
            exByGet.printStackTrace();
            throw new NoteServiceException(exByGet.getMessage());
        }
    } 
    
    public Note save(Note note) throws NoteServiceException {
        getInstanceOfDAO();
        int noteId = note.getId();
        try {
            noteDAO.get(noteId);
        } catch (NotExistDataException nedExByGet) {
            return this.create(note);
        } catch (DAOException exByGet) {
            throw new NoteServiceException(exByGet.getMessage());
        }
        try {
            noteDAO.update(note);
            noteFilterDAO.deleteAll(noteId);
            //Filter
            for(NoteFilter noteFilter: note.getFilters()) {
                noteFilterDAO.create(noteId, noteFilter);
            }
            saveBlocks(note.getId(), note.getBlocks());
            return note;
        } catch (DAOException exByUpdate) {
            exByUpdate.printStackTrace();
            throw new NoteServiceException(exByUpdate.getMessage());
        }
    }
    
    protected List<NoteBlock> getAllBlocks(int noteId) throws DAOException {
        List<NoteBlock> noteBlocks = noteBlockDAO.getAll(noteId);
        List<NoteBlock> returnBlocks = new ArrayList<>();
        for(NoteBlock noteBlock: noteBlocks) {
            int blockId = noteBlock.getId();
            switch (noteBlock.getBlockType()) {
                case TEXT -> {
                    List<TextBlock> textBlocks = textBlockDAO.getAll(blockId);
                    for(TextBlock textBlock: textBlocks) {
                        textBlock.setNoteBlock(noteBlock);
                        returnBlocks.add(textBlock);
                    }
                }

                case SURVEY -> {
                    List<SurveyBlock> surveyBlocks = surveyBlockDAO.getAll(blockId);
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
        List<NoteBlock> noteBlocks = noteBlockDAO.getAll(noteId);
        List<NoteBlock> returnBlocks = new ArrayList<>();
        for(NoteBlock noteBlock: noteBlocks) {
            int blockId = noteBlock.getId();
            switch (noteBlock.getBlockType()) {
                case TEXT -> {
                    List<TextBlock> textBlocks = textBlockDAO.getAll(blockId);
                    for(TextBlock textBlock: textBlocks) {
                        if(editor.equals(textBlock.getEditor())) {
                            textBlock.setNoteBlock(noteBlock);
                            returnBlocks.add(textBlock);
                        }
                    }
                }

                case SURVEY -> {
                    List<SurveyBlock> surveyBlocks = surveyBlockDAO.getAll(blockId);
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
        newBlock = noteBlockDAO.create(noteId, newBlock);
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
        noteBlockDAO.update(noteId, needUpdateBlock);
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
        noteBlockDAO.delete(blockId);
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