package com.example.meditation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Fogetpassword extends AppCompatActivity {
    TextView forgetpassword;
    public EditText loginEmailId;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogetpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.email);
        forgetpassword = findViewById(R.id.forget);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = loginEmailId.getText().toString();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(Fogetpassword.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }


                //reset password you will get a mail
                firebaseAuth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Fogetpassword.this, "Please check your email!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Fogetpassword.this, "Failed to reset password!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }

}
