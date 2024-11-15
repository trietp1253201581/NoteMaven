package com.noteapp.dao;

import com.noteapp.model.Note;
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
public class NoteDAO extends DAO<Note> {
    protected static final String NOTE_QUERIES_FILE_NAME = "NoteQueries.sql";

    protected static enum ColumnName {
        note_id, author, header, last_modified_date; 
    }


    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteDAO() {
        super.sqlFileName = NOTE_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
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
    public List<Note> getAll() throws DAOException {
        List<Note> notes = new ArrayList<>();
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
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt(ColumnName.note_id.toString()));
                note.setAuthor(resultSet.getString(ColumnName.author.toString()));
                note.setHeader(resultSet.getString(ColumnName.header.toString()));
                note.setLastModifiedDate(resultSet.getDate(ColumnName.last_modified_date.toString()));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<Note> getAll(DAOKey referKey) throws DAOException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_REFER.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.author.toString())) {
            throw new DAOKeyException();
        }
        String author = keyMap.get(ColumnName.author.toString());

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public Note get(DAOKey key) throws DAOException {
        Note note = new Note();
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
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt(ColumnName.note_id.toString()));
                note.setAuthor(resultSet.getString(ColumnName.author.toString()));
                note.setHeader(resultSet.getString(ColumnName.header.toString()));
                note.setLastModifiedDate(resultSet.getDate(ColumnName.last_modified_date.toString()));
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
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query, 
                    PreparedStatement.RETURN_GENERATED_KEYS);   
            preparedStatement.setString(1, newNote.getAuthor());
            preparedStatement.setString(2, newNote.getHeader());
            preparedStatement.setDate(3, newNote.getLastModifiedDate());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            newNote.setId(resultSet.getInt(1));
            return newNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public Note create(Note newNote, DAOKey key) throws DAOException {
        return this.create(newNote);
    }

    @Override
    public void update(Note note) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE.toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setString(1, note.getAuthor());
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setDate(3, note.getLastModifiedDate());
            preparedStatement.setInt(4, note.getId());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(Note note, DAOKey key) throws DAOException {
        this.update(note);
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
    
    @Override
    public void deleteAll(DAOKey referKey) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.DELETE_ALL.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.author.toString())) {
            throw new DAOKeyException();
        }
        String author = keyMap.get(ColumnName.author.toString());

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);
            //Xóa các filter của các Note tương ứng
            
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}