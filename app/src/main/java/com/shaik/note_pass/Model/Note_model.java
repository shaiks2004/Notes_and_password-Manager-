package com.shaik.note_pass.Model;

import com.google.firebase.Timestamp;

public class Note_model {
    private String tittle;
    private String descrption;
    private Timestamp timestamp;

    // Default constructor (required for Firestore)
    public Note_model() {
    }

    public Note_model(String tittle, String descrption, Timestamp timestamp) {
        this.tittle = tittle;
        this.descrption = descrption;
        this.timestamp = timestamp;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
