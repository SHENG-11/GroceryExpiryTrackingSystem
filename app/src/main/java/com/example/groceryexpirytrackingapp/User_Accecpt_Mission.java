package com.example.groceryexpirytrackingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User_Accecpt_Mission extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    TextView title, desc, point,Message;
    String Key, username;
    int points;
    String description, missiontitle, staff,status;
    AppCompatButton accept, validate;//delete mission or validate it
    DatabaseReference reference;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accecpt_mission);
        title = findViewById(R.id.mission_title3);
        desc = findViewById(R.id.mission_desc3);
        point = findViewById(R.id.mission_reward3);
        accept = findViewById(R.id.btn_accept_mission);
        validate=findViewById(R.id.btn_mission_complete);
        Message=findViewById(R.id.pendingText);

        //Bundle Handle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            InfogetAndSet(bundle);
        }
        //>>>>>>>>>>>>>>>>>>>>>>>>>

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this reference find missionlist with exact key
                reference = FirebaseDatabase.getInstance().getReference("MissionList").child(Key);
                //AlertBox dialog handle
                AlertDialog.Builder builder = new AlertDialog.Builder(User_Accecpt_Mission.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress);
                AlertDialog dialog = builder.create();
                dialog.show();
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                Mission m1 = new Mission(missiontitle, "InProgress", description, username, points);
                reference.setValue(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(User_Accecpt_Mission.this, "Missions Accepts", Toast.LENGTH_SHORT).show();
                        accept.setEnabled(false);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(User_Accecpt_Mission.this, "Mission Take Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate mean update status to "pending"
                reference = FirebaseDatabase.getInstance().getReference("MissionList").child(Key);
                //AlertBox dialog handle
                AlertDialog.Builder builder = new AlertDialog.Builder(User_Accecpt_Mission.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress);
                AlertDialog dialog = builder.create();
                dialog.show();
                //>>>>>>>>>>>>>>>>>
                Mission m1 = new Mission(missiontitle, "Pending", description, username, points);
                reference.setValue(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(User_Accecpt_Mission.this, "sent to validate", Toast.LENGTH_SHORT).show();
                        validate.setEnabled(false);
                        finish();
                    }
                });
            }
        });

    }
        public void InfogetAndSet (Bundle bundle){
            //get bundle from last activities "MissionAdapter2"
            Key = bundle.getString("Key");
            missiontitle = bundle.getString("Title");
            description = bundle.getString("Desc");
            points = bundle.getInt("Points");
            staff=bundle.getString("CurrentStaff");
            username = bundle.getString("People");
            status=bundle.getString("Status");

            //staff="All"  username="Sheng2222"
            //if status==pending cannot take cannot,cannot validate
            if (username.equals(staff)){//2222=2222
                accept.setVisibility(View.GONE);
                accept.setEnabled(false);
                if (status.equals("Pending")){
                    validate.setVisibility(View.GONE);
                    validate.setEnabled(false);
                    Message.setText("Mission Is Under Validation");
                } else if (status.equals("Completed")) {
                    validate.setVisibility(View.GONE);
                    validate.setEnabled(false);
                    Message.setText("Congratulation! You get your reward");
                } else{
                validate.setVisibility(View.VISIBLE);
                validate.setEnabled(true);
                }
            }
            else if(!username.equals(staff)) {
                accept.setVisibility(View.VISIBLE);
                accept.setEnabled(true);
                validate.setVisibility(View.GONE);
                validate.setEnabled(false);
            }
            //Set it into TV
            title.setText(missiontitle);
            desc.setText(description);
            point.setText(Integer.toString(points)+" Points");
        }
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {Toast.makeText(User_Accecpt_Mission.this, "Refresh", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            onRefresh();
        }
    }
    }