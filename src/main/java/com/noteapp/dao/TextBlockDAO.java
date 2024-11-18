package com.noteapp.dao;

import com.noteapp.model.TextBlock;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class TextBlockDAO extends DAO<TextBlock>{
    protected static final String TEXT_BLOCKS_QUERIES_FILE_NAME = "TextBlockQueries.sql";

    protected static enum ColumnName {
        block_id, editor, content;
    }
    
    private TextBlockDAO() {
        super.sqlFileName = TEXT_BLOCKS_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
    }
    
    private static class SingletonHelper {
        private static final TextBlockDAO INSTANCE = new TextBlockDAO();
    }
    
    public static TextBlockDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override
    public List<TextBlock> getAll() throws DAOException {
        List<TextBlock> textBlocks = new ArrayList<>();
        
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = enableQueries.get(QueriesType.GET_ALL.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            while (resultSet.next()) {
                TextBlock textBlock = new TextBlock();
                //Set dữ liệu cho textBlock
                textBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                textBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                textBlock.setContent(resultSet.getString(ColumnName.content.toString()));
                textBlocks.add(textBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return textBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public List<TextBlock> getAll(DAOKey referKey) throws DAOException {
        List<TextBlock> textBlocks = new ArrayList<>();
        
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = enableQueries.get(QueriesType.GET_ALL_REFER.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
        if(!keyMap.containsKey(ColumnName.block_id.toString())) {
            throw new DAOKeyException();
        }
        int blockId = Integer.parseInt(keyMap.get(ColumnName.block_id.toString()));       
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, blockId);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            while (resultSet.next()) {
                TextBlock textBlock = new TextBlock();
                //Set dữ liệu cho textBlock
                textBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                textBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                textBlock.setContent(resultSet.getString(ColumnName.content.toString()));
                textBlocks.add(textBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return textBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public TextBlock get(DAOKey key) throws DAOException {
        TextBlock textBlock = new TextBlock();
        
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = enableQueries.get(QueriesType.GET.toString());
        Map<String, String> keyMap = key.getKeyMap();
        if(!keyMap.containsKey(ColumnName.block_id.toString())) {
            throw new DAOKeyException();
        }
        if(!keyMap.containsKey(ColumnName.editor.toString())) {
            throw new DAOKeyException();
        }
        int blockId = Integer.parseInt(keyMap.get(ColumnName.block_id.toString()));        
        String editor = keyMap.get(ColumnName.editor.toString());
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, blockId);
            preparedStatement.setString(2, editor);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            while (resultSet.next()) {
                //Set dữ liệu cho textBlock
                textBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                textBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                textBlock.setContent(resultSet.getString(ColumnName.content.toString()));
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            if(textBlock.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return textBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public TextBlock create(TextBlock newTextBlock) throws DAOException {
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.CREATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newTextBlock.getId());
            preparedStatement.setString(2, newTextBlock.getEditor());
            preparedStatement.setString(3, newTextBlock.getContent());
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newTextBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public TextBlock create(TextBlock newTextBlock, DAOKey key) throws DAOException {
        return this.create(newTextBlock);
    }
    
    @Override
    public void update(TextBlock textBlock) throws DAOException {
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.UPDATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textBlock.getContent());
            preparedStatement.setInt(2, textBlock.getId());
            preparedStatement.setString(3, textBlock.getEditor());
            
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(TextBlock textBlock, DAOKey key) throws DAOException {
        this.update(textBlock);
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
        if(!keyMap.containsKey(ColumnName.editor.toString())) {
            throw new DAOKeyException();
        }
        int blockId = Integer.parseInt(keyMap.get(ColumnName.block_id.toString()));        
        String editor = keyMap.get(ColumnName.editor.toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, blockId);
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
        String query = enableQueries.get(QueriesType.DELETE.toString());
        Map<String, String> keyMap = referKey.getKeyMap();
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
}