package com.noteapp.note.dao;

import com.noteapp.common.dao.DAOKeyException;
import com.noteapp.common.dao.NotExistDataException;
import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.note.model.Note;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các phương thức thao tác dữ liệu với Note
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteDAO implements INoteDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/note/db/NoteQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";

    protected static enum ColumnName {
        note_id, author, header, last_modified_date, is_public; 
    }

    protected static enum QueriesType {
        GET_ALL_BY_AUTHOR, GET, CREATE, UPDATE, DELETE;
    }

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteDAO() {
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
        private static final NoteDAO INSTANCE = new NoteDAO();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public List<Note> getAll(String author) throws DAOException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_BY_AUTHOR.toString());
        
        if("".equals(author)) {
            throw new DAOKeyException();
        }

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, author);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt(ColumnName.note_id.toString()));
                note.setAuthor(resultSet.getString(ColumnName.author.toString()));
                note.setHeader(resultSet.getString(ColumnName.header.toString()));
                note.setLastModifiedDate(resultSet.getDate(ColumnName.last_modified_date.toString()));
                note.setPubliced(Boolean.parseBoolean(resultSet.getString(ColumnName.is_public.toString())));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public Note get(int noteId) throws DAOException {
        Note note = new Note();
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, noteId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt(ColumnName.note_id.toString()));
                note.setAuthor(resultSet.getString(ColumnName.author.toString()));
                note.setHeader(resultSet.getString(ColumnName.header.toString()));
                note.setLastModifiedDate(resultSet.getDate(ColumnName.last_modified_date.toString()));
                note.setPubliced(Boolean.parseBoolean(resultSet.getString(ColumnName.is_public.toString())));
            }
            if(note.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return note;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public Note create(Note newNote) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query, 
                    PreparedStatement.RETURN_GENERATED_KEYS);   
            preparedStatement.setString(1, newNote.getAuthor());
            preparedStatement.setString(2, newNote.getHeader());
            preparedStatement.setDate(3, newNote.getLastModifiedDate());
            preparedStatement.setString(4, String.valueOf(newNote.isPubliced()));
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()) {
                newNote.setId(resultSet.getInt(1));
            }
            return newNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void update(Note note) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE.toString());

        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);            
            preparedStatement.setString(1, note.getAuthor());
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setDate(3, note.getLastModifiedDate());
            preparedStatement.setString(4, String.valueOf(note.isPubliced()));
            preparedStatement.setInt(5, note.getId());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void delete(int noteId) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE.toString());
        
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