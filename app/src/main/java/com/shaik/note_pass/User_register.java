package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_register extends AppCompatActivity {

    EditText email, password, confirm_password;
    Button register;
    TextView back_to_login;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    // Firebase Authentication
    private FirebaseAuth auth;
    private static final String TAG = "User_register";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        email=findViewById(R.id.name);
        password=findViewById(R.id.passaword);
        confirm_password=findViewById(R.id.re_password);
        register=findViewById(R.id.button);
        back_to_login=findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(User_register.this,User_login.class);
                startActivity(intent);

            }
        });



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering, please wait...");
        progressDialog.setCancelable(false);

        // Handle Register Click
        register.setOnClickListener(v -> {
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirm_pass = confirm_password.getText().toString().trim();

            if (mail.isEmpty()) {
                email.setError("Valid email required");
                return;
            } else if (pass.isEmpty()) {
                password.setError("Valid password required");
                return;
            } else if (confirm_pass.isEmpty()) {
                confirm_password.setError("Valid confirm password required");
                return;
            } else if (pass.length() < 6 || confirm_pass.length() < 6) {
                password.setError("Password must be at least 6 characters");
                confirm_password.setError("Password must be at least 6 characters");
                return;
            } else if (!pass.equals(confirm_pass)) {
                confirm_password.setError("Passwords do not match");
                return;
            }

            // Show Progress Dialog
            progressDialog.show();

            // Firebase Authentication: Register User
            auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(User_register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(User_register.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(User_register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Registration Error", task.getException());
                        }
                    });
        });
    }
}