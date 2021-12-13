package com.erniwo.timetableconstruct.model;

public class User {
    public String name;
    public String email;
    public String type;
    public String IDNumber;

    public User() {

    }

    public User(String name, String email, String type, String IDNumber) {
        this.name = name;
        this.email = email;
        this.type = type;
        this.IDNumber = IDNumber;
    }
}
