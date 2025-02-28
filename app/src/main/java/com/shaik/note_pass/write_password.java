package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.shaik.note_pass.Model.Password_Model;

public class write_password extends AppCompatActivity {

    Button save_password;
    EditText user_name, user_uid,user_pass;
    Boolean editmode=false;
    Toolbar ttoolbar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Name,uid,password,UserId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_write_password);

        save_password=findViewById(R.id.save_pass);
        user_name=findViewById(R.id.passwrd_title);
        user_uid=findViewById(R.id.password_name);
        user_pass=findViewById(R.id.user_pass);

        ttoolbar=findViewById(R.id.ttoolbar);
        _Initoolbar();

        Name=getIntent().getStringExtra("Password_Name");
        uid=getIntent().getStringExtra("uid");
        password=getIntent().getStringExtra("password");
        UserId=getIntent().getStringExtra("userId");
        if(UserId!=null && UserId.isEmpty()){
            editmode=true;
        }
        user_name.setText(Name);
        user_uid.setText(uid);
        user_pass.setText(password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        save_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


                String name=user_name.getText().toString();
                String uid=user_uid.getText().toString();
                String pass=user_pass.getText().toString();
//                if(uid.trim().matches(emailPattern)){
//                    user_uid.setError("no strong!!");
//                }

                if (name.isEmpty()){
                    user_name.setError("Please specify a name");

                } else if (uid.isEmpty()) {
                    user_uid.setError("Fill email/uid/phone");

                } else if (pass.isEmpty()) {
                    user_pass.setError("password required");

                } else if (uid.trim().matches(emailPattern)) {
                    user_uid.setError("no strong!!");

                }else if (!pass.trim().matches(emailPattern)){
                    user_pass.setError("Password is Weak");

                }
                Password_Model passwordModel=new Password_Model();
                passwordModel.setName(name);
                passwordModel.setUid(uid);
                passwordModel.setPass(pass);
                //saviing ion to firebase dubai
                save_to_firabase_dubbai(passwordModel);

            }
        });
    }

    private void _Initoolbar() {
        setSupportActionBar(ttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Password");
        if(editmode==true){
            getSupportActionBar().setTitle("Edit Password");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void save_to_firabase_dubbai(Password_Model passwordModel) {
        //wroting the password in the firestore.
        DocumentReference docs;
        if (editmode){
            docs=Utility.passwordgetdata().document(UserId);
            ttoolbar.setTitle("Edit Password");
        }else{
            docs=Utility.passwordgetdata().document();
        }
        _Initoolbar();
        passwordModel.setTimeStamp(Timestamp.now());
        docs.set(passwordModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(write_password.this,"Password Saved",Toast.LENGTH_SHORT).show();
                    Log.d("Wrie password","the pass word is succes going :"+task.toString()+passwordModel);
                    finish();
                }
                else{
                    Toast.makeText(write_password.this,"Something Wrong",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}