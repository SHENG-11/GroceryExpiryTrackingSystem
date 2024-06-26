package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Mission extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fat;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    List<Mission> MissionList;
    MissionAdapter2 adapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission);
        recyclerView=findViewById(R.id.recycleView_item);
        fat=findViewById(R.id.btn_floating_add_mission);

        //>>end of find view of id

        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_Mission.this, Admin_Mission_Add.class);
                startActivity(intent);
            }
        });

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Admin_Mission.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //ArrayList Declear
        MissionList=new ArrayList<>();
        //the adapter we create
        adapter2=new MissionAdapter2(MissionList,Admin_Mission.this);
        recyclerView.setAdapter(adapter2); // set it into recycleview

        databaseReference= FirebaseDatabase.getInstance().getReference("MissionList");
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    //give one word, and then just set to adapter2
    }

}