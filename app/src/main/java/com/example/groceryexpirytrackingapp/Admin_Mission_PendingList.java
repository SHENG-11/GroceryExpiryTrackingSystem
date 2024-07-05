package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Mission_PendingList extends AppCompatActivity {
    RecyclerView rv_pending;
    TextView text;
    FloatingActionButton add;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    List<Mission> PendingList;
    MissionAdapter2 adapter2;
    String username;
    int isAdmin,point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission_pending_list);
        rv_pending=findViewById(R.id.recycleView_mission1);
        text=findViewById(R.id.text4);
        //Bundle get intent
        Bundle bundle=getIntent().getExtras();
        StartgetIntent(bundle);
        //>>Bundle get end
        //Bottom Navigation set
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_done);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.bottom_home){
                Intent intent=new Intent(Admin_Mission_PendingList.this, MainActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            } else if (item.getItemId()==R.id.bottom_mission) {
                Intent intent=new Intent(Admin_Mission_PendingList.this, Admin_Mission.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_done) {
                return true;
            }else if (item.getItemId()==R.id.bottom_profile) {
                Intent intent=new Intent(Admin_Mission_PendingList.this, UserProfile.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }
            return false;
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Admin_Mission_PendingList.this,1);
        rv_pending.setLayoutManager(gridLayoutManager);
        //ArrayList Declear
        PendingList=new ArrayList<>();
        rv_pending.setAdapter(adapter2); // set it into recycleview
        adapter2=new MissionAdapter2(Admin_Mission_PendingList.this,PendingList,Admin_Mission_PendingList.this,isAdmin,username,point);
        rv_pending.setAdapter(adapter2); // set it into recycleview
        databaseReference= FirebaseDatabase.getInstance().getReference("MissionList");
        if (isAdmin==0){// if the user was not admin, we set the adapter with status available and disable the floating action button
            text.setText("Accepted Mission");
            Query q1=databaseReference.orderByChild("assignTo").equalTo(username);//select mission that is accept by user
            valueEventListener=q1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PendingList.clear();
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        Mission m1=itemSnapshot.getValue(Mission.class);
                        m1.setKey(itemSnapshot.getKey());
                        PendingList.add(m1);
                    }
                    adapter2.notifyDataSetChanged();
                }
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (isAdmin==1) {
            Query q1=databaseReference.orderByChild("status").equalTo("Pending");//select mission that is accept by user
            valueEventListener=q1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PendingList.clear();
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        Mission m1=itemSnapshot.getValue(Mission.class);
                        m1.setKey(itemSnapshot.getKey());
                        PendingList.add(m1);
                    }
                    adapter2.notifyDataSetChanged();
                }
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    void StartgetIntent(Bundle bundle){
        username=bundle.getString("username");
        isAdmin=bundle.getInt("isAdmin");
        point=bundle.getInt("CurrentPoints");

    }
}