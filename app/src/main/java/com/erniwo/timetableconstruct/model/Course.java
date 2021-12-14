package com.erniwo.timetableconstruct.model;

import androidx.annotation.NonNull;

public class Course implements Cloneable, Comparable<Course>{

    private String subject;//subject name
    private String location;
    private String dayOfWeek;//value between 1-7
    private String period; //value between1-9
    private String IDNumber; // teacher's ID

    public Course() {

    }
    public Course(String subject, String location, String dayOfWeek, String period, String teacher ){

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

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
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
