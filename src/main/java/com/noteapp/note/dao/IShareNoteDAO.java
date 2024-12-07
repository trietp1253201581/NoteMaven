package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.ShareNote;
import java.util.List;
/**
 *
 * @author admin
 */
public interface IShareNoteDAO {
    List<ShareNote> getAll(String editor) throws DAOException;
    
    ShareNote get(int noteId, String editor) throws DAOException;
    
    ShareNote create(ShareNote newShareNote) throws DAOException;
    
    void update(ShareNote shareNote) throws DAOException;
    
    void delete(int noteId, String editor) throws DAOException;
    
    void deleteAll(int noteId) throws DAOException;
}
