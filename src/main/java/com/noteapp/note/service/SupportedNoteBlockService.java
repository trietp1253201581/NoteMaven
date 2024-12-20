package com.noteapp.note.service;

import com.noteapp.common.dao.DAOException;
import com.noteapp.note.dao.IConcreateBlockDAO;
import com.noteapp.note.dao.INoteBlockDAO;
import com.noteapp.note.model.NoteBlock;
import static com.noteapp.note.model.NoteBlock.BlockType.SURVEY;
import static com.noteapp.note.model.NoteBlock.BlockType.TEXT;
import com.noteapp.note.model.SurveyBlock;
import com.noteapp.note.model.TextBlock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class SupportedNoteBlockService {
    protected INoteBlockDAO noteBlockDAO;
    protected IConcreateBlockDAO<TextBlock> textBlockDAO;
    protected IConcreateBlockDAO<SurveyBlock> surveyBlockDAO;

    protected SupportedNoteBlockService(INoteBlockDAO noteBlockDAO, IConcreateBlockDAO<TextBlock> textBlockDAO, IConcreateBlockDAO<SurveyBlock> surveyBlockDAO) {
        this.noteBlockDAO = noteBlockDAO;
        this.textBlockDAO = textBlockDAO;
        this.surveyBlockDAO = surveyBlockDAO;
    }

    protected void setNoteBlockDAO(INoteBlockDAO noteBlockDAO) {
        this.noteBlockDAO = noteBlockDAO;
    }

    protected void setTextBlockDAO(IConcreateBlockDAO<TextBlock> textBlockDAO) {
        this.textBlockDAO = textBlockDAO;
    }

    protected void setSurveyBlockDAO(IConcreateBlockDAO<SurveyBlock> surveyBlockDAO) {
        this.surveyBlockDAO = surveyBlockDAO;
    }
    
    /**
     * Lấy tất cả các block của một Note
     * @param noteId id của Note chứa các block cần lấy
     * @return Một List các block của note đó (bao gồm cả các phiên bản tương
     * ứng của block)
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     * @see INoteBlockDAO#getAll(int) 
     * @see ITextBlockDAO#getAll(int) 
     * @see ISurveyBlockDAO#getAll(int) 
     * @see TextBlock
     * @see SurveyBlock
     */
    protected List<NoteBlock> getAll(int noteId) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null || textBlockDAO == null || surveyBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
        //Lấy các thông tin cơ bản của block trước
        List<NoteBlock> noteBlocks = noteBlockDAO.getAll(noteId);
        List<NoteBlock> returnBlocks = new ArrayList<>();
        //Dựa vào kiểu của block mà quyết định gọi DAO nào để lấy các phiên bản tương ứng
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
        
    /**
     * Lấy tất cả các block của một Note với một phiên bản chỉnh sửa của một User
     * @param noteId id của Note chứa các block cần lấy
     * @param editor username của chủ phiên bản chỉnh sửa này
     * @return Một List các NoteBlock của phiên bản Note tương ứng
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     * @see INoteBlockDAO#getAll(int) 
     * @see ITextBlockDAO#getAll(int) 
     * @see ISurveyBlockDAO#getAll(int) 
     * @see #getAll(int) 
     * @see TextBlock
     * @see SurveyBlock
     */
    protected List<NoteBlock> getAll(int noteId, String editor) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null || textBlockDAO == null || surveyBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
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
    
    /**
     * Tạo một block mới và lưu vào các CSDL tương ứng (NoteBlock, TextBlock, SurveyBlock)
     * @param noteId id của Note chứa mà block chuẩn bị tạo thuộc về
     * @param newBlock NoteBlock cần được tạo
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     * @see INoteBlockDAO#create(int, NoteBlock)
     * @see ITextBlockDAO#create(TextBlock) 
     * @see ISurveyBlockDAO#create(SurveyBlock) 
     * @see TextBlock
     * @see SurveyBlock
     */
    protected void create(int noteId, NoteBlock newBlock) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null || textBlockDAO == null || surveyBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
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
    
    protected void createOtherVersion(NoteBlock noteBlock, String otherEditor) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null || textBlockDAO == null || surveyBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
        noteBlock.setEditor(otherEditor);
        switch (noteBlock.getBlockType()) {
            case TEXT -> {
                TextBlock newTextBlock = (TextBlock) noteBlock;
                textBlockDAO.create(newTextBlock);
            }
            case SURVEY -> {
                SurveyBlock newSurveyBlock = (SurveyBlock) noteBlock;
                surveyBlockDAO.create(newSurveyBlock);
            }
        }
    }
    
    /**
     * Cập nhật một NoteBlock trong một Note
     * @param noteId id của Note chứa NoteBlock cần cập nhật
     * @param needUpdateBlock NoteBlock cần cập nhật
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     * @see INoteBlockDAO#update(int, NoteBlock)
     * @see ITextBlockDAO#update(TextBlock) 
     * @see ISurveyBlockDAO#update(SurveyBlock) 
     * @see TextBlock
     * @see SurveyBlock
     */
    protected void update(int noteId, NoteBlock needUpdateBlock) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null || textBlockDAO == null || surveyBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
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
    
    /**
     * Xóa một NoteBlock nào đó
     * @param blockId id của block cần xóa
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     */
    protected void delete(int blockId) throws DAOException, NoteServiceException {
        if (noteBlockDAO == null) {
            throw new NoteServiceException("DAO is null!");
        }
        noteBlockDAO.delete(blockId);
    }
    
    /**
     * Lưu một số các blocks của một Note vào CSDL tương ứng
     * @param noteId id của Note chứa các blocks cần lưu
     * @param noteBlocks các block cần lưu
     * @throws DAOException Xảy ra khi các thao tác với CSDL tương ứng
     * bị lỗi
     * @see #getAll(int, String)
     * @see #create(int, NoteBlock) 
     * @see #update(int, NoteBlock)
     * @see #delete(int) 
     * @see TextBlock
     * @see SurveyBlock
     */
    protected void save(int noteId, List<NoteBlock> noteBlocks) throws DAOException, NoteServiceException {
        //Lấy các block của phiên bản note này
        String editor = noteBlocks.get(0).getEditor();
        List<NoteBlock> blocksInDB = this.getAll(noteId, editor);
        //Phân loại các note vào loại cần tạo mới, cần cập nhật, cần xóa
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
        //Thực hiện dịch vụ tương ứng với từng loại
        for(NoteBlock newBlock: newBlocks) {
            this.create(noteId, newBlock);
        }
        for(NoteBlock needUpdateBlock: needUpdateBlocks) {
            this.update(noteId, needUpdateBlock);
        }
        for(NoteBlock deletedBlock: deletedBlocks) {
            this.delete(deletedBlock.getId());
        }
    }
}