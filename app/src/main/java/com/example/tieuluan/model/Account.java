package com.example.tieuluan.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String Password;
    private String Role;

    public Account(String id, String password, String role){
        this.id = id;
        Password = password;
        Role = role;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getPassword() {return Password;}

    public void setPassword(String password) {Password = password;}

    public String getRole() {return Role;}

    public void setRole(String role) {Role = role;}

    @Override
    public String toString() {
        return "Account{" +
                ", id='" + id + '\'' +
                ", Password='" + Password + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }

    public Account(){ }
}
