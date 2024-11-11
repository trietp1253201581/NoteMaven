package com.noteapp.dao;

import java.util.List;

/**
 * Định nghĩa các phương thức thao tác cơ bản với CSDL
 * @author Nhóm 23
 * @param <T> Kiểu datatransfer cho data từ CSDL
 * @since 30/03/2024
 * @version 1.0
 */
public interface BasicDAO<T, K, R> {
    T get(K key) throws DAOException;
    
    T add(T element) throws DAOException;
    T add(T element, K key) throws DAOException;
    
    void update(T element) throws DAOException;
    void update(T element, K key) throws DAOException;
    
    void delete(K key) throws DAOException;
    void deleteAll(R referKey) throws DAOException;
    
    List<T> getAll() throws DAOException;
    List<T> getAll(R referKey) throws DAOException;
}