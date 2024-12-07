package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOKeyException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.note.model.ShareNote;
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
public class ShareNoteDAO implements IShareNoteDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/note/db/ShareNoteQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";

    protected static enum ColumnName {
        note_id, editor, share_type; 
    }
    
    protected static enum QueriesType {
        GET_ALL_BY_EDITOR, GET, CREATE, UPDATE, DELETE, DELETE_ALL;
    }

    private ShareNoteDAO() {
        //init connection
        databaseConnection = new MySQLDatabaseConnection
            (DATABASE_HOST, DATABASE_PORT, DATABASE_NAME, 
                    DATABASE_USERNAME, DATABASE_PASSWORD);
        databaseConnection.connect();
        //Get enable Queries
        databaseConnection.readSQL(SQL_FILE_DIR);
        enableQueries = databaseConnection.getEnableQueries();
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
    public List<ShareNote> getAll(String editor) throws DAOException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_BY_EDITOR.toString());

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
    public ShareNote get(int noteId, String editor) throws DAOException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());
        if ("".equals(editor)) {
            throw new DAOKeyException();
        }

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());
        
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, newShareNote.getId());
            preparedStatement.setString(2, newShareNote.getEditor());
            preparedStatement.setString(3, newShareNote.getShareType().toString());
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newShareNote;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }

    @Override
    public void update(ShareNote shareNote) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE.toString());

        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
    public void delete(int noteId, String editor) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE.toString());
        if ("".equals(editor)) {
            throw new DAOKeyException();
        }

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
    public void deleteAll(int noteId) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE_ALL.toString());
       
        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, noteId);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}