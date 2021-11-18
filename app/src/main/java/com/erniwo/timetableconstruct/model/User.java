package com.erniwo.timetableconstruct.model;

public class User {
    public String name;
    public String email;
    public int type;

    public User() {

    }

    public User(String name, String email, int type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }
}
