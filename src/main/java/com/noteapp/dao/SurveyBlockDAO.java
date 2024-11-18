package com.noteapp.dao;

import com.noteapp.model.SurveyBlock;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class SurveyBlockDAO extends DAO<SurveyBlock>{
    protected static final String SURVEY_BLOCKS_QUERIES_FILE_NAME = "SurveyBlockQueries.sql";

    protected static enum ColumnName {
        block_id, editor, survey_map;
    }
    
    private SurveyBlockDAO() {
        super.sqlFileName = SURVEY_BLOCKS_QUERIES_FILE_NAME;
        super.initConnection();
        super.initEnableQueries();
    }
    
    private static class SingletonHelper {
        private static final SurveyBlockDAO INSTANCE = new SurveyBlockDAO();
    }
    
    public static SurveyBlockDAO getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override
    public List<SurveyBlock> getAll() throws DAOException {
        List<SurveyBlock> surveyBlocks = new ArrayList<>();
        
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
                SurveyBlock surveyBlock = new SurveyBlock();
                surveyBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                surveyBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                JSONObject surveyJSONObject = new JSONObject(resultSet.getString(ColumnName.survey_map.toString()));
                surveyBlock.setSurveyMap(surveyJSONObject);
                surveyBlocks.add(surveyBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return surveyBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public List<SurveyBlock> getAll(DAOKey referKey) throws DAOException {
        List<SurveyBlock> surveyBlocks = new ArrayList<>();
        
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
                SurveyBlock surveyBlock = new SurveyBlock();
                surveyBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                surveyBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                JSONObject surveyJSONObject = new JSONObject(resultSet.getString(ColumnName.survey_map.toString()));
                surveyBlock.setSurveyMap(surveyJSONObject);
                surveyBlocks.add(surveyBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return surveyBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public SurveyBlock get(DAOKey key) throws DAOException {
        SurveyBlock surveyBlock = new SurveyBlock();
        
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = enableQueries.get(QueriesType.GET_ALL_REFER.toString());
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
                surveyBlock.setId(resultSet.getInt(ColumnName.block_id.toString()));
                surveyBlock.setEditor(resultSet.getString(ColumnName.editor.toString()));
                JSONObject surveyJSONObject = new JSONObject(resultSet.getString(ColumnName.survey_map.toString()));
                surveyBlock.setSurveyMap(surveyJSONObject);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            if(surveyBlock.isDefaultValue()) {
                throw new NotExistDataException();
            }
            return surveyBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public SurveyBlock create(SurveyBlock newSurveyBlock) throws DAOException {
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.CREATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newSurveyBlock.getId());
            preparedStatement.setString(2, newSurveyBlock.getEditor());
            preparedStatement.setString(3, newSurveyBlock.getSurveyJSONObject().toString());
            //Chuyển từng hàng dữ liệu sang textBlock và thêm vào list
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return newSurveyBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public SurveyBlock create(SurveyBlock newSurveyBlock, DAOKey key) throws DAOException {
        return this.create(newSurveyBlock);
    }
    
    @Override
    public void update(SurveyBlock surveyBlock) throws DAOException {
        if(connection == null) {
            throw new FailedExecuteException();
        }
        
        String query = enableQueries.get(QueriesType.UPDATE.toString());
        
        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, surveyBlock.getSurveyJSONObject().toString());
            preparedStatement.setInt(2, surveyBlock.getId());
            preparedStatement.setString(3, surveyBlock.getEditor());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(SurveyBlock surveyBlock, DAOKey key) throws DAOException {
        this.update(surveyBlock);
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