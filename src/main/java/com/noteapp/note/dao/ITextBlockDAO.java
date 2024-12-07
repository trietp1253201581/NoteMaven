package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.TextBlock;
import java.util.List;

/**
 *
 * @author admin
 */
public interface ITextBlockDAO {
    List<TextBlock> getAll(int blockId) throws DAOException;
    
    void create(TextBlock newTextBlock) throws DAOException;
    
    void update(TextBlock textBlock) throws DAOException;
}
