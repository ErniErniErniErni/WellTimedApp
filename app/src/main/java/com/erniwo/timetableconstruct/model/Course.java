package com.erniwo.timetableconstruct.model;

import androidx.annotation.NonNull;

public class Course implements Cloneable, Comparable<Course>{
    private String subject;//subject name

    private String dayOfWeek;//value between 1-7
    private String period;//1-8
    private String location;
    private String teacher;

//    private int classLength = 0;//课程时长
//    private int classStart = -1;//课程开始节数

    public Course() {

    }
    public Course(String subject, String dayOfWeek, String period, String location, String teacher ){

    }

//    public int getClassStart() {
//        return classStart;
//    }
//
//    public void setClassStart(int classStart) {
//        this.classStart = classStart;
//    }
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

//    public int getClassLength() {
//        return classLength;
//    }

//    public void setClassLength(int classLength) {
//        if (classLength <= 0)
//            classLength = 1;
//        else if (classLength > 12)
//            classLength = 12;
//        this.classLength = classLength;
//    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
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
