package com.example.meditation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class mainscreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton peacetime;
    private ImageButton yoga;
    private ImageButton devo;
    TextView change_picture;
    DrawerLayout drawerLayout;
    CircleImageView profile_pic;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    static int PReqCode = 1;
    private static int PICK_IMAGE1 = 1;
    Uri profileimagePath;

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(mainscreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainscreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(mainscreen.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(mainscreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE1);
    }

    private void sendUserData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid()).child("Profile Pic");//User id/Images/Profile Pic.jpg
        final StorageReference imageReference = storageReference.child(profileimagePath.getLastPathSegment());
        imageReference.putFile(profileimagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("Error", "onComplete: image uploaded");
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        user.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            Log.e("Success", "onComplete: profileset");
                                        }

                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE1 && resultCode == RESULT_OK && data.getData() != null) {
            profileimagePath = data.getData();
            profile_pic.setImageURI(profileimagePath);
            Toast.makeText(mainscreen.this, "Picture Uploaded", Toast.LENGTH_SHORT).show();
            sendUserData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String user_name="Hello! "+user.getDisplayName();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        change_picture = headerView.findViewById(R.id.change_picture);
        profile_pic = headerView.findViewById(R.id.profilepic);

        if(user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(profile_pic);
        }
        navUsername.setText(userEmail);
        /*-----------Hooks-----------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*-----------Tool Bar-----------*/
        setSupportActionBar(toolbar);
        if(user.getDisplayName()!=null) {
            getSupportActionBar().setTitle(user_name);
        }
        /*-----------Navigation Drawer Menu-----------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //the following makes the burger menu icon work.
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        change_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });
        peacetime = (ImageButton) findViewById(R.id.button);
        peacetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainscreen.this, peacetimescreen.class);
                startActivity(i);
            }
        });
        yoga = (ImageButton) findViewById(R.id.imageView4);
        yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainscreen.this, yogascreen.class);
                startActivity(i);
            }
        });
        devo = (ImageButton) findViewById(R.id.imageView7);
        devo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainscreen.this, shlokscreen.class);
                startActivity(i);
            }
        });

    }


    //When back button is pressed, the burger menu closes if it is opened
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Menu items selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_download:
                Intent j = new Intent(mainscreen.this, downloads.class);
                startActivity(j);
                break;
            case R.id.nav_about:
                Intent k = new Intent(mainscreen.this, Aboutus.class);
                startActivity(k);
                break;
            case R.id.nav_logout:
                //logout code
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                LinearLayout LL= new LinearLayout(this);
                final TextView confirm=new TextView(this);
                confirm.setText("Do you want to contiue?");
                LL.addView(confirm);
                confirm.setMinEms(16);
                LL.setPadding(10, 10,10,10);
                builder.setView(LL);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent loginActivity = new Intent(getApplicationContext(), ActivityLogin.class);
                        startActivity(loginActivity);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey download divine app! https://play.google.com/store?hl=en_IN";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Divine");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.nav_rate:
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/"));
                startActivity(rateIntent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}