package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Mission_Validate extends AppCompatActivity {
    TextView title,desc,tvpoint;
    String username,Key="";
    int point1,points;
    String description,missiontitle,staff;
    Button delete,validate;//delete mission or validate it
    DatabaseReference reference,reference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission_validate);
        title=findViewById(R.id.mission_title2);
        desc=findViewById(R.id.mission_desc2);
        tvpoint=findViewById(R.id.mission_reward2);
        delete=findViewById(R.id.btn_delete_reject);
        validate=findViewById(R.id.btn_confirm_mission);

        //End of find view by id

        //Bundle Handle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           InfogetAndSet(bundle);
        }
        //>>>>>>>>>>>>>>>>>>>>>>>>>
        if (staff.equals("All")){
            validate.setEnabled(false);
        }else {
            validate.setEnabled(true);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference= FirebaseDatabase.getInstance().getReference("MissionList");
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Mission_Validate.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress);
                AlertDialog dialog = builder.create();
                dialog.show();
                reference.child(Key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(Admin_Mission_Validate.this, "Delete Mission Complete", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Mission_Validate.this, "Retry", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Validate button havent set
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertBox dialog handle
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Mission_Validate.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress);
                AlertDialog dialog = builder.create();
                dialog.show();
                //get current point + mission point; update user's points,and mission status
                point1=point1+points;
                reference = FirebaseDatabase.getInstance().getReference("MissionList").child(Key);
                Mission m1 = new Mission(missiontitle, "Completed", description, staff, points);
                reference.setValue(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference1=FirebaseDatabase.getInstance().getReference("UsersVer2").child(staff);
                        
                        reference1.child("points").setValue(point1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(Admin_Mission_Validate.this, "Add points successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                                Toast.makeText(Admin_Mission_Validate.this, "Mission validate fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

            });

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
        });
    }
    public void InfogetAndSet(Bundle bundle){
        //get bundle from last activities "MissionAdapter2"
        Key = bundle.getString("Key");
        missiontitle=bundle.getString("Title");
        description=bundle.getString("Desc");
        points=bundle.getInt("Points");//Mission Point
        point1=bundle.getInt("CurrentPoints");//Acc current point
        staff=bundle.getString("People");//specific users
        username=bundle.getString("CurrentStaff"); //current users
        //Set it into TV
        title.setText(missiontitle);
        desc.setText(description);
        tvpoint.setText(Integer.toString(points));
    }
}