package com.noteapp.dao;

import com.noteapp.model.NoteFilter;
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
public class NoteFilterDAO extends DAO<NoteFilter>{
    protected static final String NOTE_FILTERS_QUERIES_FILE_NAME = "NoteFilterQueries.sql";

    protected static enum ColumnName {
        note_id, filter;
    }

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteFilterDAO() {
        super.sqlFileName = NOTE_FILTERS_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
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
    public List<NoteFilter> getAll() throws DAOException {
        List<NoteFilter> noteFilters = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL.toString());

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public List<NoteFilter> getAll(DAOKey referKey) throws DAOException {
        List<NoteFilter> noteFilters = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET_ALL_REFER.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
    public NoteFilter get(DAOKey key) throws DAOException {
        NoteFilter noteFilter = new NoteFilter();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        if(!keyMap.containsKey(ColumnName.filter.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        String filter = keyMap.get(ColumnName.filter.toString());
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, filter);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteFilter và thêm vào list
            while (resultSet.next()) {
                //Set dữ liệu cho noteFilter
                noteFilter.setFilter(resultSet.getString(ColumnName.filter.toString()));
            }    
            //Nếu không tồn tại thì ném ngoại lệ
            if(noteFilter.isDefaultValue()) {
                throw new NotExistDataException();
            }   
            return noteFilter;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteFilter create(NoteFilter newNoteFilter) throws DAOException {
        throw new UnsupportedQueryException();
    }
    
    @Override
    public NoteFilter create(NoteFilter newNoteFilter, DAOKey key) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.CREATE_KEY.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, newNoteFilter.getFilter());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newNoteFilter;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(NoteFilter noteFilter) throws DAOException {
        throw new UnsupportedQueryException();
    }
    
    @Override
    public void update(NoteFilter noteFilter, DAOKey key) throws DAOException {
        throw new UnsupportedQueryException();
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
        if(!keyMap.containsKey(ColumnName.filter.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        String filter = keyMap.get(ColumnName.filter.toString());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, filter);

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
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
