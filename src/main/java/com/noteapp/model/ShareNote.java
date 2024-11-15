package com.noteapp.model;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note để chia sẻ giữa các user
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */

public class ShareNote extends Note {
    private String receiver;
    private ShareType shareType;
    
    /**
     * Định nghĩa các kiểu Share
     */
    public static enum ShareType {
        READ_ONLY, CAN_EDIT;
    }
    
    /**
     * Constructor và cài đặt dữ liệu default cho ShareNote
     */
    public ShareNote() {
        super();
        this.receiver = "";
        this.shareType = ShareType.READ_ONLY;
    }

    public ShareNote(String receiver, ShareType shareType) {
        super();
        this.receiver = receiver;
        this.shareType = shareType;
    }

    public ShareNote(String receiver, ShareType shareType, int id, String author, String header, List<NoteBlock> blocks, Date lastModifiedDate, List<NoteFilter> filters) {
        super(id, author, header, blocks, lastModifiedDate, filters);
        this.receiver = receiver;
        this.shareType = shareType;
    }

    public String getReceiver() {
        return receiver;
    }
    
    public ShareType getShareType() {
        return shareType;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.receiver);
        hash = 53 * hash + Objects.hashCode(this.shareType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ShareNote other = (ShareNote) obj;
        final Note otherNote = (Note) other;
        if(!super.equals(otherNote)) {
            return false;
        }
        if(!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        return this.shareType == other.shareType;
    }
    
    /**
     * Set giá trị note cho các thuộc tính tương ứng của sharenote
     * @param note note cần set up
     */
    public void setNote(Note note) {
        super.setId(note.getId());
        super.setAuthor(note.getAuthor());
        super.setHeader(note.getHeader());
        super.setBlocks(note.getBlocks());
        super.setLastModifiedDate(note.getLastModifiedDate());
        super.setFilters(note.getFilters());
    }

    @Override
    public String toString() {
        return "ShareNote{" + "receiver=" + receiver + ", shareType=" + shareType + '}';
    }
}