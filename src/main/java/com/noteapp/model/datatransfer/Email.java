package com.noteapp.model.datatransfer;

import com.noteapp.model.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class Email extends BaseDataTransferModel {
    private String address;
    private String name;
    
    public Email() {
        this.address = "";
        this.name = "";
    }

    public Email(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean isDefaultValue() {
        return "".equals(address);
    }
    
    public boolean checkEmailAddress() {
        String emailAddStrPatern = "^([a-zA-Z0-9._]+)@([a-zA-Z0-9_.]+)\\.([a-zA-Z]{2,})$";
        Pattern emailAddPattern = Pattern.compile(emailAddStrPatern);
        Matcher matcher = emailAddPattern.matcher(this.address);
        return matcher.matches();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.address);
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final Email other = (Email) obj;
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }
    
    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("address", this.address);
        attributeMap.put("name", this.name);
        return attributeMap;
    }
    
    @Override
    public String toString() {        
        String objectName = "Email";
        return super.toString(objectName);
    }
    
    public static Email valueOf(String str) {
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static Email valueOf(Map<String, String> attributeStrMap) {
        Email email = new Email();
        email.setAddress(attributeStrMap.get("address"));
        email.setName(attributeStrMap.get("name"));
        return email;
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends User> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<Email> convertToList(String listOfEmailStr) {
            List<Map<String, String>> listOfEmailMaps = Command.decode(listOfEmailStr);
            List<Email> emails = new ArrayList<>();
            for(Map<String, String> emailMap: listOfEmailMaps) {
                Email newEmail = Email.valueOf(emailMap);
                emails.add(newEmail);
            }
            return emails;
        }
    }
}
