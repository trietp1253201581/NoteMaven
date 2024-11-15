/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteapp.dao;

/**
 *
 * @author admin
 */
public class TestDAO {
    public static void main(String[] args) {
        NoteBlockDAO userDAO = NoteBlockDAO.getInstance();
        try {
            DAOKey key = new DAOKey();
            key.addKey("note_id", "2030");
            System.out.println(userDAO.getAll(key).get(0));
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }
}
