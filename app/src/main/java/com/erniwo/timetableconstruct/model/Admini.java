package com.erniwo.timetableconstruct.model;

public class Admini {

    private String name;
    private String email;
    private String idnumber;
    private String uid;

    public Admini() {

    }
    public Admini(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public Admini(String name, String email, String idnumber){
        this.name = name;
        this.email = email;
        this.idnumber = idnumber;
    }
    public Admini(String name, String email, String idnumber, String uid){
        this.name = name;
        this.email = email;
        this.idnumber = idnumber;
        this.uid = uid;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIDNumber() {
        return idnumber;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIDNumber(String IDNumber) {
        this.idnumber = IDNumber;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
