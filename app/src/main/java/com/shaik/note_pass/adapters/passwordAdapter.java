package com.shaik.note_pass.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.shaik.note_pass.Model.Password_Model;
import com.shaik.note_pass.R;
import com.shaik.note_pass.Utility;
import com.shaik.note_pass.write_password;

import java.util.concurrent.Executor;

public class passwordAdapter extends FirestoreRecyclerAdapter<Password_Model, passwordAdapter.passwordViewHolder> {
    Context context;

    public passwordAdapter(@NonNull FirestoreRecyclerOptions<Password_Model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull passwordViewHolder passwordViewHolder, int i, @NonNull Password_Model passwordModel) {
        passwordViewHolder.Password_Name.setText(passwordModel.getName());
        passwordViewHolder.uid.setText(passwordModel.getUid());
        passwordViewHolder.password.setText(passwordModel.getPass());
        if (passwordModel.getTimeStamp() != null) {
            passwordViewHolder.timeStamp.setText(Utility.time(passwordModel.getTimeStamp()));
        } else {
            passwordViewHolder.timeStamp.setText("No Date Available");
        }

        String userId = getSnapshots().getSnapshot(i).getId();

        passwordViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show biometric authentication prompt
                showBiometricPrompt(passwordModel, userId);
            }
        });
    }

    private void showBiometricPrompt(Password_Model passwordModel, String userId) {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt((androidx.fragment.app.FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Biometric authentication succeeded, navigate to write_password activity
                navigateToWritePassword(passwordModel, userId);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Biometric authentication failed
                Toast.makeText(context, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Biometric authentication error
                Toast.makeText(context, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        // Build the biometric prompt dialog
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate to view the password")
                .setNegativeButtonText("Cancel")
                .build();

        // Show the biometric prompt
        biometricPrompt.authenticate(promptInfo);
    }

    private void navigateToWritePassword(Password_Model passwordModel, String userId) {
        Intent intent = new Intent(context, write_password.class);
        intent.putExtra("Password_Name", passwordModel.getName());
        intent.putExtra("uid", passwordModel.getUid());
        intent.putExtra("password", passwordModel.getPass());
        intent.putExtra("timeStamp", passwordModel.getTimeStamp());
        intent.putExtra("userId", userId);

        Log.d("PasswordAdapter: ", "Getting the document id from Firestore: " + passwordModel.getName());
        Log.d("PasswordAdapter: ", "Getting the document pass from Firestore: " + passwordModel.getPass());
        Log.d("PasswordAdapter: ", "Getting the document uid from Firestore: " + passwordModel.getUid());
        Log.d("PasswordAdapter: ", "Getting the document time from Firestore: " + passwordModel.getTimeStamp());

        context.startActivity(intent);
    }

    @NonNull
    @Override
    public passwordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_password_items, viewGroup, false);
        return new passwordViewHolder(view);
    }

    class passwordViewHolder extends RecyclerView.ViewHolder {
        TextView password, uid, Password_Name, timeStamp;

        public passwordViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            Password_Name = itemView.findViewById(R.id.Password_Name);
            uid = itemView.findViewById(R.id.uid);
            password = itemView.findViewById(R.id.password);
        }
    }
}