package com.noteapp.model;

/**
 * Một abstract class đại diện cho các DataTransfer Object
 * để lưu giữ dữ liệu từ CSDL
 * @author admin
 * @version 1.0
 */
public abstract class DTO {
    
    /**
     * Kiểm tra một {@link DTO} có mang giá trị mặc định không
     * @return {@code true} nếu DTO này là mặc định, ngược lại là {@code false}
     */
    public abstract boolean isDefaultValue();
}