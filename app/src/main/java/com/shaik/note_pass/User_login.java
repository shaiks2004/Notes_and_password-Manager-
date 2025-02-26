package com.shaik.note_pass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class User_login extends AppCompatActivity {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "User_login";
    EditText email, password;
    Button login;
    TextView signup;
    ProgressDialog progressDialog;

    //firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading! Plase Wait");
        progressDialog.setCancelable(true);
        signup = findViewById(R.id.signup);


        //get the id of the xml
        email = findViewById(R.id.gmail);
        password = findViewById(R.id.passaword);
        login = findViewById(R.id.button);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Login Succesfull", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"login succesfull");
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, User_register.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) ->
                {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty()) {
                    email.setError("Enter Email");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Password lenght must be greater than 6 characters");
                    progressDialog.dismiss();
                    System.out.println("Email:" + email.getText().toString());
                    System.out.println("Password:" + password.getText().toString());

                } else {
                    progressDialog.show();
                    String Mail = email.getText().toString();
                    String pass = password.getText().toString();

                    auth.signInWithEmailAndPassword(Mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            Log.d(TAG,"singning with email and password"+auth);
                            Log.d(TAG, "Login Acticyty in queue:" + auth);
                            Log.d(TAG, "Login Acticyty in queue:" + task.toString());
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(User_login.this, MainActivity.class);
                                startActivity(intent);
                                Log.d(TAG,"login succesfull");
                                finish();
                            } else {
                                Toast.makeText(User_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "LOGIN ACTIViTY FAILED" + task.getException().toString());

                            }

                        }
                    });
                }

            }

        });

    }
}