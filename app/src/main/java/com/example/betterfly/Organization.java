package com.example.betterfly;

import java.io.Serializable;
import java.util.ArrayList;

public class Organization implements Serializable {
    public String name , email , approvalId , discreption , password;
    public Status status;
    public ArrayList<event> eventList;


    public Organization(){

    }


    public Organization(String name, String email, String password , String approvalId, Status status ,String discription, ArrayList<event> eventList) {
        this.name = name;
        this.email = email;
        this.approvalId = approvalId;
        this.status = status;
        this.password = password;
        this.discreption = discription;
        this.eventList = new ArrayList<event>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public String getDiscreption() {
        return discreption;
    }

    public Status getStatus() {
        return status;
    }

    public ArrayList<event> getEventList() {
        return eventList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public void setDiscreption(String discreption) {
        this.discreption = discreption;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setEventList(ArrayList<event> eventList) {
        this.eventList = eventList;
    }
}
