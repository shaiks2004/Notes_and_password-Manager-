package com.shaik.note_pass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shaik.note_pass.Model.Note_model;
import com.shaik.note_pass.adapters.NoteAdapter;

public class Notes_display extends AppCompatActivity {

    FloatingActionButton add_btn;
    RecyclerView recyclerView;
    Toolbar toolbar;
    NoteAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_display);

        add_btn = findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
        InitToolbar();

        // Set up RecyclerView
        setupRecyclerView();

        // Floating Action Button Click
        add_btn.setOnClickListener(v -> {
            Animation animation;
            Intent intent = new Intent(Notes_display.this, notes_details.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        Query query = Utility.getdata().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note_model> options = new FirestoreRecyclerOptions.Builder<Note_model>()
                .setQuery(query, Note_model.class)
                .build();

        adapter = new NoteAdapter(options, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Debugging: Check Firestore data
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.e("Firestore", "No notes found in Firestore.");
                } else {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.d("FirestoreData", "Note: " + doc.getData());
                    }
                }
            } else {
                Log.e("Firestore", "Error fetching notes: " + task.getException().getMessage());
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
    private void InitToolbar() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText("Note Manager");


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
}

