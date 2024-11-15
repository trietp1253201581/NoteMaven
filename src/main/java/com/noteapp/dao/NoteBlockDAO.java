package com.noteapp.dao;

import com.noteapp.model.NoteBlock;
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

public class NoteBlockDAO extends DAO<NoteBlock>{
    protected static final String NOTE_BLOCKS_QUERIES_FILE_NAME = "NoteBlockQueries.sql";

    protected static enum ColumnName {
        block_id, note_id, header, block_type, block_order;
    }


    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteBlockDAO() {
        super.sqlFileName = NOTE_BLOCKS_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
    }

    private static class SingletonHelper {
        private static final NoteBlockDAO INSTANCE = new NoteBlockDAO();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteBlockDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override 
    public List<NoteBlock> getAll() throws DAOException {
        List<NoteBlock> noteBlocks = new ArrayList<>();
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
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                NoteBlock noteBlock = new NoteBlock();
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                noteBlock.setHeader(resultSet.getString(ColumnName.header.toString()));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString(ColumnName.block_type.toString())));
                noteBlock.setOrder(resultSet.getInt(ColumnName.block_order.toString()));
                noteBlocks.add(noteBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return noteBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public List<NoteBlock> getAll(DAOKey referKey) throws DAOException {
        List<NoteBlock> noteBlocks = new ArrayList<>();
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
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                NoteBlock noteBlock = new NoteBlock();
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                noteBlock.setHeader(resultSet.getString(ColumnName.header.toString()));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString(ColumnName.block_type.toString())));
                noteBlock.setOrder(resultSet.getInt(ColumnName.block_order.toString()));
                noteBlocks.add(noteBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return noteBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteBlock get(DAOKey key) throws DAOException {
        NoteBlock noteBlock = new NoteBlock();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.GET.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.block_id.toString())) {
            throw new DAOKeyException();
        }
        int blockId = Integer.parseInt(keyMap.get(ColumnName.block_id.toString()));
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, blockId);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                noteBlock.setHeader(resultSet.getString(ColumnName.header.toString()));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString(ColumnName.header.toString())));
                noteBlock.setOrder(resultSet.getInt(ColumnName.block_order.toString()));
            }    
            //Nếu không tồn tại thì ném ngoại lệ
            if(noteBlock.isDefaultValue()) {
                throw new NotExistDataException();
            }   
            return noteBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteBlock create(NoteBlock newNoteBlock) throws DAOException {
        throw new UnsupportedQueryException();
    }
    
    @Override
    public NoteBlock create(NoteBlock newNoteBlock, DAOKey key) throws DAOException {
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
            PreparedStatement preparedStatement = connection.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, newNoteBlock.getHeader());
            preparedStatement.setString(3, newNoteBlock.getBlockType().toString());
            preparedStatement.setInt(4, newNoteBlock.getOrder());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            newNoteBlock.setId(resultSet.getInt(1));
            return newNoteBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    @Override
    public void update(NoteBlock noteBlock) throws DAOException {
        throw new UnsupportedQueryException();
    }
    
    @Override
    public void update(NoteBlock noteBlock, DAOKey key) throws DAOException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = enableQueries.get(QueriesType.UPDATE_KEY.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.note_id.toString())) {
            throw new DAOKeyException();
        }
        int noteId = Integer.parseInt(keyMap.get(ColumnName.note_id.toString()));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn 
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, noteBlock.getHeader());
            preparedStatement.setString(3, noteBlock.getBlockType().toString());
            preparedStatement.setInt(4, noteBlock.getOrder());
            preparedStatement.setInt(5, noteBlock.getId());
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
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
        if(!keyMap.containsKey(ColumnName.block_id.toString())) {
            throw new DAOKeyException();
        }
        int blockId = Integer.parseInt(keyMap.get(ColumnName.block_id.toString()));

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, blockId);

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
        String query = enableQueries.get(QueriesType.DELETE.toString());
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
