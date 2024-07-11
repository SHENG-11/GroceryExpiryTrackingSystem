package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.groceryexpirytrackingapp.databinding.ActivityAdminMissionBinding;
import com.example.groceryexpirytrackingapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Mission_Add extends AppCompatActivity {
TextInputEditText title,desc,rewardPoint;
AppCompatButton add_mission;
DatabaseReference reference;
Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission_add);
        title=findViewById(R.id.MissionTitle);
        desc=findViewById(R.id.MissionDesc);
        rewardPoint=findViewById(R.id.MissionReward);
        add_mission=findViewById(R.id.btn_add_mission);
        //>>Find view by id end here

        add_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    UploadToFireBase();
                    //to thank you page
                }
                else {
                    Toast.makeText(Admin_Mission_Add.this, "Add Mission Fail", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    boolean validation() {

        if (title.length() == 0) {
            title.setError("This field is require");
            return false;
        }
        if (desc.length() == 0) {
            desc.setError("This field is require");
            return false;
        }
        if (rewardPoint.length() == 0) {
            rewardPoint.setError("This field is require");
            return false;
        }

        return true;
    }

    void UploadToFireBase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Mission_Add.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();
        String mission_title=title.getText().toString();
        String mission_desc=desc.getText().toString();
        int rewardPoint1=Integer.parseInt((rewardPoint.getText().toString()));
        Mission m1=new Mission(mission_title,"Available",mission_desc,"All",rewardPoint1);
        reference = FirebaseDatabase.getInstance().getReference("MissionList");
        reference.child(mission_title).setValue(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.dismiss();
                Toast.makeText(Admin_Mission_Add.this, "Mission Add Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(Admin_Mission_Add.this, "Mission Add Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

}