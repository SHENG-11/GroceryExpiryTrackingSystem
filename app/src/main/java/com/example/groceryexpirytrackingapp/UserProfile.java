package com.example.groceryexpirytrackingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryexpirytrackingapp.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class UserProfile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    DatabaseReference reference;
    StorageReference storageReference;
    String username;
    int isAdmin,point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_profile);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Bundle get intent
        Bundle bundle=getIntent().getExtras();
        StartgetIntent(bundle);
        //Bottom Navigation set
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.bottom_home){
                Intent intent=new Intent(UserProfile.this, MainActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            } else if (item.getItemId()==R.id.bottom_mission) {
                Intent intent=new Intent(UserProfile.this, Admin_Mission.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_done) {
                Intent intent=new Intent(UserProfile.this, Admin_Mission_PendingList.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_profile) {
                return true;
            }
            return false;
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>
        /*
        Intent intent = getIntent();
        username = intent.getStringExtra("username");//geting string from previous activity
        isAdmin=intent.getIntExtra("isAdmin",0);
        point=intent.getIntExtra("CurrentPoints",0);
         */



    }

    private void readUser(String username) {//Funtion of reading user information form realtime database
        reference = FirebaseDatabase.getInstance().getReference("UsersVer2");//geting database references
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+username + ".jpg");
        reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {//username in child is getting from start of this activities, it contain username in string data type
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {//if geting username successful do the task below
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();//data snapshot contain data from a firebase database location
                        //String username = String.valueOf(dataSnapshot.child("username").getValue());
                        String phonenumber = String.valueOf(dataSnapshot.child("phoneNumber").getValue());
                        String fullname = String.valueOf(dataSnapshot.child("fullname").getValue());
                        int point=dataSnapshot.child("points").getValue(Integer.class);
                        binding.fullname1.setText(fullname);
                        binding.username1.setText(username);
                        binding.hpNum1.setText(phonenumber);
                        binding.pointBonus.setText(Integer.toString(point));
                        try {
                            File localfile = File.createTempFile("tempfile", ".jpg");
                            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    binding.userPicture1.setImageBitmap(bitmap);
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }
    void StartgetIntent(Bundle bundle){
            username=bundle.getString("username");
            isAdmin=bundle.getInt("isAdmin");
            point=bundle.getInt("CurrentPoints");

        if (username != null) {
            readUser(username);//calling function at below
        } else {
            Toast.makeText(this, "Please relogin to the system", Toast.LENGTH_SHORT).show();
        }

    }
}