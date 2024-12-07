package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.Note;
import java.util.List;

/**
 *
 * @author admin
 */
public interface INoteDAO {
    List<Note> getAll(String author) throws DAOException;
    
    Note get(int noteId) throws DAOException;
    
    Note create(Note newNote) throws DAOException;
    
    void update(Note note) throws DAOException;
    
    void delete(int noteId) throws DAOException;
}