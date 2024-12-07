package com.noteapp.note.dao;

import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.note.model.NoteFilter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class NoteFilterDAO implements INoteFilterDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/note/db/NoteFilterQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";

    protected static enum ColumnName {
        note_id, filter;
    }
    
    protected static enum QueriesType {
        GET_ALL_BY_NOTE, CREATE, DELETE_ALL;
    }

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteFilterDAO() {
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
        private static final NoteFilterDAO INSTANCE = new NoteFilterDAO();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteFilterDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override 
    public List<NoteFilter> getAll(int noteId) throws DAOException {
        List<NoteFilter> noteFilters = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_BY_NOTE.toString());

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteFilter và thêm vào list
            while (resultSet.next()) {
                NoteFilter noteFilter = new NoteFilter();
                //Set dữ liệu cho noteFilter
                noteFilter.setFilter(resultSet.getString(ColumnName.filter.toString()));
                noteFilters.add(noteFilter);
            }    
            //Nếu noteFilters rỗng thì ném ngoại lệ là danh sách trống
            return noteFilters;
        }  catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void create(int noteId, NoteFilter newNoteFilter) throws DAOException {
        //Kiểm tra null
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE.toString());
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, newNoteFilter.getFilter());
            
            if(preparedStatement.executeUpdate() <= 0) {
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
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, noteId);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}