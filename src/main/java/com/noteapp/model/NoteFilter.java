package com.noteapp.model;

import java.util.Objects;

/**
 *
 * @author admin
 */
public class NoteFilter extends DTO {
    private String filterContent;

    public NoteFilter() {
        this.filterContent = "";
    }

    public NoteFilter(String filterContent) {
        this.filterContent = filterContent;
    }

    public String getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(String filterContent) {
        this.filterContent = filterContent;
    }

    public boolean isDefaultValue() {
        return "".equals(this.filterContent);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.filterContent);
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
        final NoteFilter other = (NoteFilter) obj;
        return Objects.equals(this.filterContent, other.filterContent);
    }

    @Override
    public String toString() {
        return "NoteFilter{" + "filterContent=" + filterContent + '}';
    }

}
