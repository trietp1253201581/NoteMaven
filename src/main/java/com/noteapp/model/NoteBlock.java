package com.noteapp.model;

import java.util.Objects;

/**
 *
 * @author admin
 */
public class NoteBlock extends DTO {
    private int id;
    private String header;
    private String editor;
    private BlockType blockType;
    private int order;
    
    public static enum BlockType {
        TEXT, SURVEY
    }
    
    public NoteBlock() {
        this.id = -1;
        this.order = -1;
        this.header = "";
        this.editor = "";
        this.blockType = BlockType.TEXT;
    }

    public NoteBlock(int id, String header, String editor, BlockType blockType, int order) {
        this.id = id;
        this.header = header;
        this.editor = editor;
        this.blockType = blockType;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    /**
     * Kiểm tra xem một thể hiện Note có mang giá trị default không
     * @return (1) {@code true} nếu đây là default Note, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return id == -1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.header);
        hash = 71 * hash + Objects.hashCode(this.editor);
        hash = 71 * hash + Objects.hashCode(this.blockType);
        hash = 71 * hash + this.order;
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
        final NoteBlock other = (NoteBlock) obj;
        if (this.order != other.order) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.editor, other.editor)) {
            return false;
        }
        return this.blockType == other.blockType;
    }

    @Override
    public String toString() {
        return "NoteBlock{" + "id=" + id + ", header=" + header + ", editor=" + editor + ", blockType=" + blockType + ", order=" + order + '}';
    }
}