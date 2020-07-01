package com.example.meditation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signup extends AppCompatActivity {
    public EditText emailId, passwd, passwd2, username;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    private CheckBox checkbox;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        passwd2 = findViewById(R.id.ETpasswords);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.TVSignIn);
        checkbox =  findViewById(R.id.checkBox);
        username=findViewById(R.id.username);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    // hide password


                    passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                String paswd2 = passwd2.getText().toString();
                String username1 = username.getText().toString();

                if (username1.isEmpty()) {
                    username.setError("Provide your Username first!");
                    username.requestFocus();
                }else if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if(!emailID.trim().matches(emailPattern))
                {
                    emailId.setError("Enter valid email address!");
                    emailId.requestFocus();
                }
                else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                }else if (paswd2.isEmpty()) {
                    passwd2.setError("Confirm your password");
                    passwd2.requestFocus();
                }
                else if (!paswd.equals(paswd2)) {
                    passwd2.setError("Password doesn't match");
                    passwd2.requestFocus();
                }
                else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(signup.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(signup.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username1)
                                        .build();
                                user.updateProfile(profleUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    // user info updated successfully
                                                    Log.e("Success", "onComplete: profileset"+user.getDisplayName());
                                                    startActivity(new Intent(signup.this, mainscreen.class));
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    Toast.makeText(signup.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(signup.this, ActivityLogin.class);
                startActivity(I);
            }
        });
    }
}
