package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shaik.note_pass.Model.Password_Model;
import com.shaik.note_pass.adapters.passwordAdapter;

public class Password_display extends AppCompatActivity {

    Toolbar toolBar;
    RecyclerView recyclerView;
    FloatingActionButton new_pas;
    passwordAdapter adapter;
    TextView Password_Name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_display);
        recyclerView=findViewById(R.id.recyclerView);
        Password_Name=findViewById(R.id.Password_Name);
        toolBar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolBar);
        new_pas=findViewById(R.id.new_pass);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupRecylerView();
        new_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Password_display.this,write_password.class);
                startActivity(intent);
            }

        });
    }

    private void setupRecylerView() {
        Query query=Utility.passwordgetdata().orderBy("timeStamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Password_Model> options=new FirestoreRecyclerOptions.Builder<Password_Model>()
                .setQuery(query,Password_Model.class)
                .build();

        adapter=new passwordAdapter(options,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//        Password_Name.setTextColor(Color.parseColor("#EB9934"));


        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.e("Firestore", "No password found in Firestore.");
                } else {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.d("FirestoreData", "Password: " + doc.getData());
                    }
                }
            } else {
                Log.e("Firestore", "Error fetching password: " + task.getException().getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }





//    private void setSupportActionBar(Toolbar toolBar) {
//        setSupportActionBar(toolBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        getSupportActionBar().setTitle("Password Manger");
//    }


}
package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shaik.note_pass.Model.Password_Model;
import com.shaik.note_pass.adapters.passwordAdapter;

public class Password_display extends AppCompatActivity {

    Toolbar toolBar;
    RecyclerView recyclerView;
    FloatingActionButton new_pas;
    passwordAdapter adapter;
    TextView Password_Name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_display);

        recyclerView = findViewById(R.id.recyclerView);
        Password_Name = findViewById(R.id.Password_Name);
        toolBar = findViewById(R.id.toolbar);
        new_pas = findViewById(R.id.new_pass);

        // ✅ Fix Toolbar
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Password Manager");

        toolBar.setNavigationOnClickListener(v -> finish());  // Back button functionality

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();

        new_pas.setOnClickListener(v -> {
            Intent intent = new Intent(Password_display.this, write_password.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        Query query = Utility.passwordgetdata().orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Password_Model> options = new FirestoreRecyclerOptions.Builder<Password_Model>()
                .setQuery(query, Password_Model.class)
                .build();

        adapter = new passwordAdapter(options, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Password_Model password = documentSnapshot.toObject(Password_Model.class);
            Intent intent = new Intent(Password_display.this, write_password.class);
            intent.putExtra("password_id", documentSnapshot.getId());  // Send document ID
            intent.putExtra("password_name", password.getPasswordName()); // Send password name
            intent.putExtra("password_value", password.getPasswordValue()); // Send actual password
            startActivity(intent);
        });

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.e("Firestore", "No password found in Firestore.");
                } else {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.d("FirestoreData", "Password: " + doc.getData());
                    }
                }
            } else {
                Log.e("Firestore", "Error fetching password: " + task.getException().getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}
