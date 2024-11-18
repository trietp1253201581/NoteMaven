package com.noteapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class SurveyBlock extends NoteBlock {
    private Map<String, Boolean> surveyMap;

    public SurveyBlock() {
        super();
        this.surveyMap = new HashMap<>();
    }

    public SurveyBlock(Map<String, Boolean> surveyMap) {
        super();
        this.surveyMap = surveyMap;
    }

    public SurveyBlock(Map<String, Boolean> surveyMap, int id, String header, String editor, BlockType blockType, int order) {
        super(id, header, editor, blockType, order);
        this.surveyMap = surveyMap;
    }

    public Map<String, Boolean> getSurveyMap() {
        return surveyMap;
    }

    public void setSurveyMap(Map<String, Boolean> surveyMap) {
        this.surveyMap = surveyMap;
    }
    
    public void takeSurvey(String optionName, boolean option) {
        surveyMap.put(optionName, option);
    }
    
    public void setNoteBlock(NoteBlock noteBlock) {
        super.setId(noteBlock.getId());
        super.setHeader(noteBlock.getHeader());
        super.setBlockType(noteBlock.getBlockType());
        super.setOrder(noteBlock.getOrder());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.surveyMap);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurveyBlock other = (SurveyBlock) obj;
        final NoteBlock otherNoteBlock = (NoteBlock) other;
        if(!super.equals(otherNoteBlock)) {
            return false;
        }
        return Objects.equals(this.surveyMap, other.surveyMap);
    }

    @Override
    public String toString() {
        return "SurveyBlock{" + "content=" + surveyMap + '}';
    }
    
    public JSONObject getSurveyJSONObject() {
        return new JSONObject(surveyMap);
    }
    
    public void setSurveyMap(JSONObject surveyJSONObject) {
        Map<String, Boolean> nowSurveyMap = new HashMap<>();
        Iterator<String> keys = surveyJSONObject.keys();
        
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                boolean value = surveyJSONObject.getBoolean(key);
                nowSurveyMap.put(key, value);
            } catch (JSONException ex) {
                System.err.println(ex.getMessage());
            }
        }
        this.surveyMap = nowSurveyMap;
    }
}