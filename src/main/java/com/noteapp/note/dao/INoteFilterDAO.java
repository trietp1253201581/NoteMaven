package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.NoteFilter;
import java.util.List;

/**
 *
 * @author admin
 */
public interface INoteFilterDAO {
    List<NoteFilter> getAll(int noteId) throws DAOException;
    
    void create(int noteId, NoteFilter newNoteFilter) throws DAOException;
    
    void deleteAll(int noteId) throws DAOException;
}
