package com.shaik.note_pass;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static CollectionReference passwordgetdata(){
        FirebaseUser passwordUser=FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Password").document(passwordUser.getEmail()).collection("My_Passwords");
    }

    static CollectionReference getdata() {
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes").document(currentuser.getEmail()).collection("My_notes");
    }

    static String time(Timestamp timestamp) {
        return new SimpleDateFormat("MM/DD/YYYY").format(timestamp.toDate());
    }
}
