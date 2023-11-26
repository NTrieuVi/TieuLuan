package com.example.tieuluan.model;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private String id;
    private String Avatar;
    //private String StudentId;
    private String Name;
    private String Gender;
    private String Phone;
    private String Department;


    public Student(String id, String avatar, String name, String gender, String phone, String departmnet){
        this.id = id;
        Avatar = avatar;
        //StudentId = studentId;
        Name = name;
        Gender = gender;
        Phone = phone;
        Department = departmnet;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return Avatar;
    }
    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }
    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDepartment() {
        return Department;
    }
    public void setDepartment(String department) {
        Department = department;
    }

    @Override
    public String toString() {
        return "Student{" +
                "Avatar='" + Avatar + '\'' +
                ", id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Department='" + Department + '\'' +
                '}';
    }

    public Student(){ }
}
