package com.noteapp.dao;

import com.noteapp.model.ShareNote;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các phương thức thao tác dữ liệu với ShareNote
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */
public class ShareNoteDAO extends DAO<ShareNote> {
    protected static final String SHARE_NOTE_QUERIES_FILE_NAME = "ShareNoteQueries.sql";

    protected static enum ColumnName {
        note_id, editor, share_type; 
    }

    private ShareNoteDAO() {
        super.sqlFileName = SHARE_NOTE_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
    }
    
    private static class SingletonHelper {
        public static final ShareNoteDAO INSTANCE = new ShareNoteDAO();
    }
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static ShareNoteDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public List<ShareNote> getAll() throws DAOException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL.toString());

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                shareNote.setId(resultSet.getInt(ColumnName.note_id.toString()));
                shareNote.setEditor(resultSet.getString(ColumnName.editor.toString()));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString(ColumnName.share_type.toString())));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<ShareNote> getAll(DAOKey referKey) throws DAOException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_REFER.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.editor.toString())) {
            throw new DAOKeyException();
        }
        String editor = keyMap.get(ColumnName.editor.toString());

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, editor);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                shareNote.setId(resultSet.getInt(ColumnName.note_id.toString()));
                shareNote.setEditor(resultSet.getString(ColumnName.editor.toString()));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString(ColumnName.share_type.toString())));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public ShareNote get(DAOKey key) throws DAOException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        if(!keyMap.containsKey(ColumnName.editor.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        String editor = keyMap.get(ColumnName.editor.toString());

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, editor);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {                
                shareNote.setId(resultSet.getInt(ColumnName.note_id.toString()));
                shareNote.setEditor(resultSet.getString(ColumnName.editor.toString()));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString(ColumnName.share_type.toString())));
            }
            if(shareNote.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return shareNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public ShareNote create(ShareNote newShareNote) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newShareNote.getId());
            preparedStatement.setString(2, newShareNote.getEditor());
            preparedStatement.setString(3, newShareNote.getShareType().toString());
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newShareNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public ShareNote create(ShareNote newShareNote, DAOKey key) throws DAOException {
        return this.create(newShareNote);
    }

    @Override
    public void update(ShareNote shareNote) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE.toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shareNote.getShareType().toString());
            preparedStatement.setInt(2, shareNote.getId());
            preparedStatement.setString(3, shareNote.getEditor());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(ShareNote shareNote, DAOKey key) throws DAOException {
        this.update(shareNote);
    }

    @Override
    public void delete(DAOKey key) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        if(!keyMap.containsKey(ColumnName.editor.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        String editor = keyMap.get(ColumnName.editor.toString());

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, editor);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(DAOKey referKey) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE_ALL.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}