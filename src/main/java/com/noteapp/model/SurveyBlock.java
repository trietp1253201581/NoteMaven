package com.noteapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public SurveyBlock(Map<String, Boolean> content) {
        super();
        this.surveyMap = content;
    }

    public SurveyBlock(Map<String, Boolean> content, int id, String header, String editor, BlockType blockType, int order) {
        super(id, header, editor, blockType, order);
        this.surveyMap = content;
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
        super.setEditor(noteBlock.getEditor());
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
}
