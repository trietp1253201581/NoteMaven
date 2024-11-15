/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.dao.connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

/**
 *
 * @author admin
 */
public abstract class DatabaseConnection {
    protected String url;
    protected String username;
    protected String password;
    protected Map<String, String> enableQueries;
    
    protected static final String COMMENT_PREFIX = "--";
    protected static final String LINE_DELIMITER = " ";
    
    public abstract Connection getConnection();
    
    public void readSQL(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String queryName = null;
            StringBuilder query = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                if(line.trim().startsWith(COMMENT_PREFIX)) {
                    if(queryName != null) {
                        enableQueries.put(queryName, query.toString().trim());
                        query.setLength(0);
                    }
                    queryName = line.trim().substring(2).trim();
                } else {
                    query.append(line);
                    query.append(LINE_DELIMITER);
                }
            }
        } catch (FileNotFoundException ex1) {
            System.err.println(ex1.getMessage());
        } catch (IOException ex2) {
            System.err.println(ex2.getMessage());
        }
    }

    public Map<String, String> getEnableQueries() {
        return enableQueries;
    }
}
