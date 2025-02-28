package com.shaik.note_pass.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.shaik.note_pass.Model.Note_model;
import com.shaik.note_pass.R;
import com.shaik.note_pass.notes_details;


public class NoteAdapter extends FirestoreRecyclerAdapter<Note_model, NoteAdapter.NoteviewHolder> {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note_model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteviewHolder holder, int position, @NonNull Note_model note) {
        holder.title.setText(note.getTittle());
        holder.descrption.setText(note.getDescrption());

        // Retrieve the Firestore document ID
        String docId = getSnapshots().getSnapshot(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, notes_details.class);
                intent.putExtra("title", note.getTittle());
                intent.putExtra("descrption", note.getDescrption());
//                intent.putExtra("timestamp",note.getTimeStamp());
                intent.putExtra("docId", docId); // Correct Firestore doc ID
                Log.d("NOTEadpater: ", "geting the document id from firestire: "+note.getTittle());
                Log.d("NOTEadpater: ", "geting the document id from firestire: "+note.getDescrption());


                Log.d("NOTEadpater: ", "geting the document id from firestire: "+docId);
                context.startActivity(intent);
            }
        });

        // If you need to display timestamp
        // holder.note_time.setText(note.getTimestamp().toDate().toString());
    }
    @NonNull
    @Override
    public NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list_item, parent, false);
        return new NoteviewHolder(view);
    }

    static class NoteviewHolder extends RecyclerView.ViewHolder {
        TextView title, descrption, note_time;

        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            descrption = itemView.findViewById(R.id.description);
//            note_time = itemView.findViewById(R.id.notetime);
        }
    }
}
