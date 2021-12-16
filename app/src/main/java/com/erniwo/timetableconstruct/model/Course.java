package com.erniwo.timetableconstruct.model;

import androidx.annotation.NonNull;

public class Course implements Cloneable, Comparable<Course>{

    private String subject;//subject name
    private String location;
    private String dayOfWeek;//value between 1-7
    private String period; //value between1-9
    private String idnumber; // teacher's ID
    private String classid; // class's ID

    public Course() {

    }
    public Course(String subject, String location, String dayOfWeek, String period, String teacher, String classid ){

    }

    public String getPeriod() {return period;}

    public void setPeriod(String period) {this.period = period;}

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(Course o) {
        return 0;
    }
}
