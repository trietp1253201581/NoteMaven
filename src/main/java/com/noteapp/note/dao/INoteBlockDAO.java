package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.NoteBlock;
import java.util.List;
/**
 *
 * @author admin
 */
public interface INoteBlockDAO {
    List<NoteBlock> getAll(int noteId) throws DAOException;
    
    NoteBlock create(int noteId, NoteBlock newNoteBlock) throws DAOException;
    
    void update(int noteId, NoteBlock noteBlock) throws DAOException;
    
    void delete(int blockId) throws DAOException;
}