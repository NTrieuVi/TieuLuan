package com.example.tieuluan.model;

import java.io.Serializable;

public class User implements Serializable {
    private String imageUrl;
    private String Image;
    private String id;
    private String Name;
    private String Age;
    private String Phone;
    private String Status;

    public User(String id,String image, String name, String age, String phone, String status) {
        Image = image;
        Name = name;
        Age = age;
        Phone = phone;
        Status = status;
        this.id=id;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "Image='" + Image + '\'' +
                ", id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Age='" + Age + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }

    public User() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}