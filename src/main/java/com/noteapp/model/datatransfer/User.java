package com.noteapp.model.datatransfer;

import com.noteapp.model.Command;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class User extends BaseDataTransferModel {
    private String name;
    private String username;
    private String password;
    private Date birthday;
    private String school;
    private Gender gender;
    private Email email;
    
    /**
     * Các giới tính
     */
    public static enum Gender {
        MALE, FEMALE, OTHER;
    }

    /**
     * Constructor và cài đặt dữ liệu default cho User
     */
    public User() {
        this.name = "";
        this.username = "";
        this.password = "";
        this.birthday = Date.valueOf(LocalDate.MIN);
        this.school = "";
        this.gender = Gender.MALE;
    }

    public User(int id, String name, String username, String password, Date birthday, String school, Gender gender) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.school = school;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
    
    /**
     * Kiểm tra xem một thể hiện User có mang giá trị default không
     * @return (1) {@code true} nếu đây là default User, (2) {@code false} nếu ngược lại
     */
    @Override
    public boolean isDefaultValue() {
        return "".equals(username);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.username);
        hash = 41 * hash + Objects.hashCode(this.password);
        hash = 41 * hash + Objects.hashCode(this.birthday);
        hash = 41 * hash + Objects.hashCode(this.school);
        hash = 41 * hash + Objects.hashCode(this.gender);
        hash = 41 * hash + Objects.hashCode(this.email);
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
        final User other = (User) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.school, other.school)) {
            return false;
        }
        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        return Objects.equals(this.email, other.email);
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("name", this.name);
        attributeMap.put("username", this.username);
        attributeMap.put("password", this.password);
        attributeMap.put("birthday", this.birthday);
        attributeMap.put("school", this.school);
        attributeMap.put("gender", this.gender);
        attributeMap.put("email", this.email);
        return attributeMap;
    }

    /**
     * Chuyển một User thành String
     * @return String thu được, có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;><///><///>"}
     * trong đó * đại diện cho các thuộc tính
     */
    @Override
    public String toString() {        
        String objectName = "User";
        return super.toString(objectName);
    }
    
    /**
     * Chuyển một String sang một User
     * @param str String cần chuyển có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>///"}
     * trong đó * đại diện cho các thuộc tính
     * @return User thu được
     */
    public static User valueOf(String str) {       
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static User valueOf(Map<String, String> attributeStrMap) {
        User user = new User();
        user.setName(attributeStrMap.get("name"));
        user.setUsername(attributeStrMap.get("username"));
        user.setPassword(attributeStrMap.get("password"));
        user.setBirthday(Date.valueOf(attributeStrMap.get("birthday")));
        user.setSchool(attributeStrMap.get("school"));
        user.setGender(Gender.valueOf(attributeStrMap.get("gender")));
        user.setEmail(Email.valueOf(attributeStrMap.get("email")));
        
        return user;   
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends User> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<User> convertToList(String listOfUserStr) {
            List<Map<String, String>> listOfUserMaps = Command.decode(listOfUserStr);
            List<User> users = new ArrayList<>();
            for(Map<String, String> userMap: listOfUserMaps) {
                User newUser = User.valueOf(userMap);
                users.add(newUser);
            }
            return users;
        }
    }
}