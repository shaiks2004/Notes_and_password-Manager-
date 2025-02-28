package com.shaik.note_pass;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.shaik.note_pass.Model.Note_model;

public class notes_details extends AppCompatActivity {

    ImageView save_btn;
    EditText notes_tittle, notes_descrption;
    Toolbar toolbar;
    TextView main_toolbar_title;
    String Titile,description,docId;
    ImageButton delete;


    boolean editmode=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_details);

        save_btn = findViewById(R.id.save_btn);
        notes_tittle = findViewById(R.id.notes_tittle);
        notes_descrption = findViewById(R.id.notes_descrption);
        toolbar = findViewById(R.id.toolbar);
        main_toolbar_title=findViewById(R.id.main_toolbar_title);
        delete=findViewById(R.id.delete_note);

        Titile=getIntent().getStringExtra("title");
        description=getIntent().getStringExtra("descrption");
        docId=getIntent().getStringExtra("docId");
        Log.d("notes_details", "searchning for wherr the data is coming" +Titile+description+docId);


        if(docId!=null && !docId.isEmpty()){
            editmode=true;
            Log.d("notes_deatials", "onCreate: "+editmode);
        }
        notes_tittle.setText(Titile);
        notes_descrption.setText(description);
        if (editmode){
            main_toolbar_title.setText("Edit Note");
            Log.d("notes_details", "searchning for wherr the data is coming" +editmode);
            delete.setVisibility(VISIBLE);
        }
        InitToolbar();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notes_tittle.getText().toString();
                String content = notes_descrption.getText().toString();

                if (title.isEmpty()) {
                    notes_tittle.setError("Please fill Title");
                    return;
                }

                Note_model noteModel = new Note_model();
                noteModel.setTittle(title);
                noteModel.setDescrption(content);
                noteModel.setTimestamp(Timestamp.now());

                saveNoteToFirebase(noteModel);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_note();
            }
        });

    }




    private void delete_note() {
        DocumentReference docs = Utility.getdata().document(docId);



        docs.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(notes_details.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("Notes Details", "error while delteinng: " + task.toString());
                }
            }
        });

    }

    void saveNoteToFirebase(Note_model noteModel) {
        DocumentReference docs;
        if (editmode){
           docs = Utility.getdata().document(docId);
        }else{
         docs= Utility.getdata().document();
        }


        noteModel.setTimestamp(Timestamp.now());
        docs.set(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(notes_details.this, "Note saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("Notes Details", "Note not added: " + task.toString());
                }
            }
        });
    }
    private void InitToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText("Add Note");
        if(editmode==true){
            main_toolbar_title.setText("Edit text");
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

}
