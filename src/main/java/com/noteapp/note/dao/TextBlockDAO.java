package com.noteapp.note.dao;

import com.noteapp.common.dao.FailedExecuteException;
import com.noteapp.common.dao.DAOException;
import com.noteapp.common.dbconnection.MySQLDatabaseConnection;
import com.noteapp.common.dbconnection.SQLDatabaseConnection;
import com.noteapp.note.model.TextBlock;
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
public class TextBlockDAO implements ITextBlockDAO {
    protected SQLDatabaseConnection databaseConnection;
    protected Map<String, String> enableQueries;
    
    protected static final String SQL_FILE_DIR = "src/main/java/com/noteapp/note/db/TextBlockQueries.sql";

    protected static final String DATABASE_HOST = "localhost";  
    protected static final int DATABASE_PORT = 3306;
    protected static final String DATABASE_NAME = "notelitedb";
    protected static final String DATABASE_USERNAME = "root";
    protected static final String DATABASE_PASSWORD = "Asensio1234@";

    protected static enum ColumnName {
        block_id, editor, content;
    }
    
    protected static enum QueriesType {
        GET_ALL_BY_BLOCK, CREATE, UPDATE;
    }
    
    private TextBlockDAO() {
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
        private static final TextBlockDAO INSTANCE = new TextBlockDAO();
    }
    
    public static TextBlockDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public List<TextBlock> getAll(int blockId) throws DAOException {
        List<TextBlock> textBlocks = new ArrayList<>();
        
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        String query = enableQueries.get(QueriesType.GET_ALL_BY_BLOCK.toString());  
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
    public void create(TextBlock newTextBlock) throws DAOException {
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.CREATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, newTextBlock.getId());
            preparedStatement.setString(2, newTextBlock.getEditor());
            preparedStatement.setString(3, newTextBlock.getContent());
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void update(TextBlock textBlock) throws DAOException {
        if(databaseConnection.getConnection() == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.UPDATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
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
}