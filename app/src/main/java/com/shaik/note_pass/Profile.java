package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    Toolbar toolbar;
    TextView name, email, password;
    FirebaseAuth firebaseAuth;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        InitToolbar();
        email = findViewById(R.id.user_email);
        name = findViewById(R.id.user_name);
        password = findViewById(R.id.user_password);
        listView=findViewById(R.id.profile_list);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if(auth!=null){
            firebaseUser.getEmail();
            firebaseUser.getDisplayName();
            firebaseUser.getUid();
            firebaseUser.getDisplayName();

            email.setText(firebaseUser.getEmail());
            name.setText(firebaseUser.getDisplayName());
            password.setText(firebaseUser.getUid());
            System.out.println(email+"\n"+name+"\n"+password);
            Log.d("Profile.java","the profile java showing"+email.toString()+" "+name.toString()+" "+password);
        }
        setSystemDetails();
    }

    @SuppressLint("HardwareIds")
    private void setSystemDetails() {
        List<String> systemDetails = new ArrayList<>();

        systemDetails.add("Brand: " + android.os.Build.BRAND);
        systemDetails.add("DeviceID: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        systemDetails.add("Model: " + android.os.Build.MODEL);
        systemDetails.add("ID: " + android.os.Build.ID);
        systemDetails.add("SDK: " + android.os.Build.VERSION.SDK_INT);
        systemDetails.add("Manufacturer: " + android.os.Build.MANUFACTURER);
        systemDetails.add("User: " + android.os.Build.USER);
        systemDetails.add("Type: " + android.os.Build.TYPE);
        systemDetails.add("Base: " + android.os.Build.VERSION_CODES.BASE);
        systemDetails.add("Incremental: " + android.os.Build.VERSION.INCREMENTAL);
        systemDetails.add("Board: " + android.os.Build.BOARD);
        systemDetails.add("Host: " + android.os.Build.HOST);
        systemDetails.add("Fingerprint: " + android.os.Build.FINGERPRINT);
        systemDetails.add("Version Code: " + android.os.Build.VERSION.RELEASE);

        // Bind data to ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, systemDetails);
        listView.setAdapter(adapter);
    }


    private void InitToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }
    }
}