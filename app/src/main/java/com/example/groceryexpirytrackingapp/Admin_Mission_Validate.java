package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Mission_Validate extends AppCompatActivity {
    TextView title,desc,point;
    String Key="";
    String description,missiontitle,points,staff;
    Button delete,validate;//delete mission or validate it
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission_validate);
        title=findViewById(R.id.mission_title2);
        desc=findViewById(R.id.mission_desc2);
        point=findViewById(R.id.mission_reward2);
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
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference= FirebaseDatabase.getInstance().getReference("MissionList");
                reference.child(Key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Admin_Mission_Validate.this, "Delete Mission Complete", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(Admin_Mission_Validate.this, Admin_Mission.class);
                        startActivity(intent);
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


    }
    public void InfogetAndSet(Bundle bundle){
        //get bundle from last activities "MissionAdapter2"
        Key = bundle.getString("Key");
        missiontitle=bundle.getString("Title");
        description=bundle.getString("Desc");
        points=bundle.getString("Points");
        staff=bundle.getString("People");
        //Set it into TV
        title.setText(missiontitle);
        desc.setText(description);
        point.setText(points);
    }
}