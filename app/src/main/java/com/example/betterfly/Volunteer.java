package com.example.betterfly;

import java.util.Date;

public class Volunteer {
    public String name , email, phone;
    public Date dob;

    public  String getPhone() {
        return phone;
    }

    public  void setPhone(String phone) {
        this.phone = phone;
    }

    public Volunteer(){

    }

    public  void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Volunteer(String name, String email, Date dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }


    public String getName() {
        return name;
    }
}
