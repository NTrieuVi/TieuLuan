package com.example.tieuluan.model;

import java.io.Serializable;
import java.util.Date;

public class Certificate implements Serializable {
    private String id;
    private String Name;
    private String Level;
    private String DateStart;
    private String StudentId;

    public Certificate(String id, String name, String level, String dateStart, String studentId){
        this.id = id;
        Name = name;
        Level = level;
        DateStart = dateStart;
        StudentId = studentId;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {return Name;}

    public void setName(String name) {Name = name;}

    public String getLevel() {return Level;}

    public void setLevel(String level) {Level = level;}

    public String getDateStart() {return DateStart;}

    public void setDateStart(String dateStart) {DateStart = dateStart;}

    public String getStdId() {return StudentId;}

    public void setStdId(String stdId) {StudentId = stdId;}

    @Override
    public String toString() {
        return "Certificate{" +
                ", id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Level='" + Level + '\'' +
                ", DateStart='" + DateStart + '\'' +
                ", StudentId='" + StudentId + '\'' +
                '}';
    }

    public Certificate(){ }
}
