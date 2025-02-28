package com.shaik.note_pass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.shaik.note_pass.Model.Password_Model;
import com.shaik.note_pass.adapters.passwordAdapter;

public class Password_display extends AppCompatActivity {

    Toolbar toolBar;
    RecyclerView recyclerView;
    FloatingActionButton new_pas;
    passwordAdapter adapter;
    TextView Password_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_display);
        recyclerView = findViewById(R.id.recyclerView);
        toolBar= findViewById(R.id.ttoolbar);

        _IntiToolbar();


       new_pas = findViewById(R.id.new_pass);
        setupRecylerView();
        new_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Password_display.this, write_password.class);
                startActivity(intent);
            }
        });
    }
    private void _IntiToolbar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Password Manager");

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

    private void setupRecylerView() {
        Query query = Utility.passwordgetdata().orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Password_Model> options = new FirestoreRecyclerOptions.Builder<Password_Model>()
                .setQuery(query, Password_Model.class)
                .build();

        adapter = new passwordAdapter(options, this);
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


}


