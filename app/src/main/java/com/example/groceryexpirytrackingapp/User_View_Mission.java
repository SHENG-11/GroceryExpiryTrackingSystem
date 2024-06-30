package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User_View_Mission extends AppCompatActivity {
    RecyclerView rv_mission;
    List<Mission> MissionList;
    DatabaseReference databaseReference;
    MissionAdapter2 adapter2;
    String username="";
    int admin;
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_mission);
        rv_mission=findViewById(R.id.rv_user_mission);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(User_View_Mission.this,1);
        rv_mission.setLayoutManager(gridLayoutManager);
        //ArrayList Declear
        MissionList=new ArrayList<>();
        //the adapter we create
        //Get Bundle information;
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            //Get bundle information


            username=bundle.getString("username");
            admin=bundle.getInt("isAdmin");
            adapter2=new MissionAdapter2(MissionList,User_View_Mission.this,admin,username);
            rv_mission.setAdapter(adapter2); // set it into recycleview
            databaseReference= FirebaseDatabase.getInstance().getReference("MissionList");

            if (admin==0){
                Query q1=databaseReference.orderByChild("status").equalTo("Available");
                valueEventListener=q1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MissionList.clear();
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                            Mission m1=itemSnapshot.getValue(Mission.class);
                            m1.setKey(itemSnapshot.getKey());
                            MissionList.add(m1);
                        }
                        adapter2.notifyDataSetChanged();
                    }
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (admin==1) {
                valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MissionList.clear();
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                            Mission m1=itemSnapshot.getValue(Mission.class);
                            m1.setKey(itemSnapshot.getKey());
                            MissionList.add(m1);
                        }
                        adapter2.notifyDataSetChanged();
                    }
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            Toast.makeText(this, "bundle get", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "bundle error", Toast.LENGTH_SHORT).show();
        }


    }
}