package com.shaik.note_pass.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.shaik.note_pass.Model.Password_Model;
import com.shaik.note_pass.Password_display;
import com.shaik.note_pass.R;

public class passwordAdapter extends FirestoreRecyclerAdapter<Password_Model,passwordAdapter.passwordViewHolder>{
    Context context;
    public passwordAdapter(@NonNull FirestoreRecyclerOptions<Password_Model> options, Context context) {
        super(options);
        this.context= context;

    }

    @Override
    protected void onBindViewHolder(@NonNull passwordViewHolder passwordViewHolder, int i, @NonNull Password_Model passwordModel) {
        passwordViewHolder.Password_Name.setText(passwordModel.getName());
        passwordViewHolder.uid.setText(passwordModel.getUid());
        passwordViewHolder.password.setText(passwordModel.getPass());
//        passwordViewHolder.timeStamp.setText(passwordModel.getTimeStamp());
        String userId=getSnapshots().getSnapshot(i).getId();
        passwordViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Password_display.class);
                intent.putExtra("Password_Name",passwordModel.getName());
                intent.putExtra("uid",passwordModel.getUid());
                intent.putExtra("password",passwordModel.getPass());
                intent.putExtra("timeStamp",passwordModel.getTimeStamp());
                intent.putExtra("userId",userId);
                Log.d("PasswordAdapter: ", "geting the document id from firestire: "+passwordModel.getName());
                Log.d("PasswordAdapter: ", "geting the document pass from firestire: "+passwordModel.getPass());
                Log.d("PasswordAdapter: ", "geting the document uid from firestire: "+passwordModel.getUid());
                Log.d("PasswordAdapter: ", "geting the document time from firestire: "+passwordModel.getTimeStamp());
                context.startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public passwordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_password_items,viewGroup,false);

        return new passwordViewHolder(view);
    }

    class passwordViewHolder extends RecyclerView.ViewHolder{

        TextView password,uid,Password_Name,timeStamp;
        public passwordViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp=itemView.findViewById(R.id.timeStamp);
            Password_Name=itemView.findViewById(R.id.Password_Name);
            uid=itemView.findViewById(R.id.uid);
            password=itemView.findViewById(R.id.password);

        }
    }

}