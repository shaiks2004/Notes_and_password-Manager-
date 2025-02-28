package com.shaik.note_pass.Model;

import com.google.firebase.Timestamp;

public class Password_Model {
    private String name;
    private String uid;
    private String pass;
    private Timestamp timeStamp;

    //defalut costructi=or for storing password.
    public Password_Model() {
    }

    public Password_Model(String name, String uid, String pass, Timestamp timeStamp) {
        this.name = name;
        this.uid = uid;
        this.pass = pass;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Timestamp getTimeStamp() { return timeStamp; }
    public void setTimeStamp(Timestamp timeStamp) { this.timeStamp = timeStamp; }
}

