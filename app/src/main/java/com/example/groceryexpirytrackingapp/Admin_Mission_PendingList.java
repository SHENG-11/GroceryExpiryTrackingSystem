package com.example.groceryexpirytrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Mission_PendingList extends AppCompatActivity {
    RecyclerView rv_pending;
    FloatingActionButton add;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    List<Mission> PendingList;
    MissionAdapter2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission_pending_list);
        rv_pending=findViewById(R.id.recycleView_mission1);
        add=findViewById(R.id.btn_floating_add_mission1);
        //Floting action button handle
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_Mission_PendingList.this, Admin_Mission_Add.class);
                startActivity(intent);
            }
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        GridLayoutManager gridLayoutManager=new GridLayoutManager(Admin_Mission_PendingList.this,1);
        rv_pending.setLayoutManager(gridLayoutManager);
        //ArrayList Declear
        PendingList=new ArrayList<>();
        rv_pending.setAdapter(adapter2); // set it into recycleview

    }
}