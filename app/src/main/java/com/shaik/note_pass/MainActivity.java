package com.shaik.note_pass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    LinearLayout notes,passaword;
    ImageView menu,note_img,pass_img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        notes=findViewById(R.id.notes);
        passaword=findViewById(R.id.passaword);
        menu=findViewById(R.id.menu_btn);
        note_img=findViewById(R.id.notes_img);
        pass_img=findViewById(R.id.passwor_img);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Notes_display.class);
                startActivity(intent);
            }
        });

        passaword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Password_display.class);
                startActivity(intent);
            }
        });
        //animation tutroilas
        Animation shake= AnimationUtils.loadAnimation(this,R.anim.milkshake);
        menu.setAnimation(shake);;
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu();
            }
        });
    }

    private void popupMenu() {
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menu);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Profile");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, User_login.class));
                    finish();
                    return true;
                }
                if (item.getTitle()=="Profile"){
                    Intent intent=new Intent(MainActivity.this,Profile.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}