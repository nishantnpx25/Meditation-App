package com.example.meditation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    LinearLayout clayout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth =  FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null)
//        {
//            startActivity(new Intent(this, Yoga.class));
//            this.finish();
//        }
//        else
//        {
//            startActivity(new Intent(this, mainscreen.class));
//            this.finish();
//        }

        clayout = findViewById(R.id.linearLayout);
        clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null)
                {
                    startActivity(new Intent(MainActivity.this, Yoga.class));
                    MainActivity.this.finish();
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, mainscreen.class));
                    MainActivity.this.finish();
                }

            }
        });

    }
    public class User{

    }
}
