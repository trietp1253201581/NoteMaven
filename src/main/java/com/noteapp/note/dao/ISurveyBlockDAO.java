package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.model.SurveyBlock;
import java.util.List;

/**
 *
 * @author admin
 */
public interface ISurveyBlockDAO {
    List<SurveyBlock> getAll(int blockId) throws DAOException;
    
    void create(SurveyBlock newTextBlock) throws DAOException;
    
    void update(SurveyBlock textBlock) throws DAOException;
}