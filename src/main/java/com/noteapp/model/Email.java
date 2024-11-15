package com.noteapp.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class Email {
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
    public String toString() {
        return "Email{" + "address=" + address + ", name=" + name + '}';
    }
}
