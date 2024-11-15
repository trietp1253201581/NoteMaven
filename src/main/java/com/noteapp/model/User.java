package com.noteapp.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class User extends DTO {
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
    public String toString() {
        return "User{" + "name=" + name + ", username=" + username + ", password=" + password + ", birthday=" + birthday + ", school=" + school + ", gender=" + gender + ", email=" + email + '}';
    }

}