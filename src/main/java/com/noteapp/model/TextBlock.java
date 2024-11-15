package com.noteapp.model;

import java.util.Objects;

/**
 *
 * @author admin
 */
public class TextBlock extends NoteBlock {
    private String content;

    public TextBlock() {
        super();
    }

    public TextBlock(String content) {
        super();
        this.content = content;
    }

    public TextBlock(String content, int id, String header, String editor, BlockType blockType, int order) {
        super(id, header, editor, blockType, order);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.content);
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
        final TextBlock other = (TextBlock) obj;
        final NoteBlock otherNoteBlock = (NoteBlock) other;
        if(!super.equals(otherNoteBlock)) {
            return false;
        }
        return Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return "TextBlock{" + "content=" + content + '}';
    }
}
