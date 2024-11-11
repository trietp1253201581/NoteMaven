package com.noteapp.model;

/**
 * Class để lưu các thuộc tính network để server và client sử dụng
 * @author Nhóm 23
 * @since 08/05/2024
 * @version 1.0
 */
public class NetworkProperty {
    
    /**
     * Tín hiệu ngắt kết nối để kết thúc luồng gửi từ client tới server
     */
    public static final String END_CONNECTION_SIGNAL = "<end>";
    
    /**
     * Cổng kết nối bên server
     */
    public static final int PORT_NUMBER = 2222;
    
    /**
     * Địa chỉ của máy chứa Application Server
     */
    public static final String SERVER_ADDRESS = "localhost";
    
    /**
     * Host của CSDL
     */
    public static final String DATABASE_HOST = "localhost";
    
    /**
     * Cổng phục vụ kết nối CSDL
     */
    public static final int DATABASE_PORT = 3306;
    
    /**
     * Tên CSDL
     */
    public static final String DATABASE_NAME = "notelitedb";
    
    /**
     * Username của CSDL
     */
    public static final String DATABASE_USERNAME = "root";
    
    /**
     * Password để kết nối CSDL
     */
    public static final String DATABASE_PASSWORD = "Asensio1234@";
}