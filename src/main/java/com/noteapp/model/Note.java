package com.noteapp.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class Note extends DTO {
    private int id;
    private String author;
    private String header;
    private List<NoteBlock> blocks;
    private Date lastModifiedDate;
    private List<NoteFilter> filters;
    
    /**
     * Constructor và cài đặt dữ liệu default cho Note
     */
    public Note() {
        this.id = -1;
        this.author = "";
        this.header = "";
        this.blocks = new ArrayList<>();
        this.lastModifiedDate = Date.valueOf(LocalDate.MIN);
        this.filters = new ArrayList<>();
    }

    public Note(int id, String author, String header, List<NoteBlock> blocks, Date lastModifiedDate, List<NoteFilter> filters) {
        this.id = id;
        this.author = author;
        this.header = header;
        this.blocks = blocks;
        this.lastModifiedDate = lastModifiedDate;
        this.filters = filters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<NoteBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<NoteBlock> blocks) {
        this.blocks = blocks;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<NoteFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<NoteFilter> filters) {
        this.filters = filters;
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
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.author);
        hash = 73 * hash + Objects.hashCode(this.header);
        hash = 73 * hash + Objects.hashCode(this.blocks);
        hash = 73 * hash + Objects.hashCode(this.lastModifiedDate);
        hash = 73 * hash + Objects.hashCode(this.filters);
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
        final Note other = (Note) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.blocks, other.blocks)) {
            return false;
        }
        if (!Objects.equals(this.lastModifiedDate, other.lastModifiedDate)) {
            return false;
        }
        return Objects.equals(this.filters, other.filters);
    }

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", author=" + author + ", header=" + header + ", blocks=" + blocks + ", lastModifiedDate=" + lastModifiedDate + ", filters=" + filters + '}';
    }
}
