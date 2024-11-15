package com.noteapp.dao;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
public class DAOKey {
    private Map<String, String> keyMap;
    
    public DAOKey() {
        keyMap = new HashMap<>();
    }

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }
    
    public void addKey(String keyId, String keyValue) {
        keyMap.put(keyId, keyValue);
    }
}
