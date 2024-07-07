package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Mission extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    FloatingActionButton fat;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    List<Mission> MissionList;
    MissionAdapter2 adapter2;
    String username="";
    int isAdmin,point;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mission);
        //Find view by id
        recyclerView=findViewById(R.id.recycleView_item);
        fat=findViewById(R.id.btn_floating_add_mission);
        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        //>>end of find view of id
        //Bundle get intent
        Bundle bundle=getIntent().getExtras();
        StartgetIntent(bundle);
        //Bottom Navigation set
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_mission);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.bottom_home){
                Intent intent=new Intent(Admin_Mission.this, MainActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            } else if (item.getItemId()==R.id.bottom_mission) {
                return true;
            }else if (item.getItemId()==R.id.bottom_done) {
                Intent intent=new Intent(Admin_Mission.this, Admin_Mission_PendingList.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_profile) {
                Intent intent=new Intent(Admin_Mission.this, UserProfile.class);
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
        recyclerView.setAdapter(adapter2); // set it into recycleview


        if (bundle!=null){
            //Get bundle information
            username=bundle.getString("username");
            isAdmin=bundle.getInt("isAdmin");
            point=bundle.getInt("CurrentPoints");
            adapter2=new MissionAdapter2(Admin_Mission.this,MissionList,Admin_Mission.this,isAdmin,username,point);
            recyclerView.setAdapter(adapter2); // set it into recycleview

            databaseReference= FirebaseDatabase.getInstance().getReference("MissionList");
            if (isAdmin==0){// if the user was not admin, we set the adapter with status available and disable the floating action button
                fat.setVisibility(View.GONE);
                fat.setEnabled(false);

            }else if (isAdmin==1) {
                fat.setVisibility(View.VISIBLE);
                fat.setEnabled(true);
            }
        }

        Query q1=databaseReference.orderByChild("status").equalTo("Available");//select miss
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


    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
            Toast.makeText(Admin_Mission.this, "Refresh", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }


    void StartgetIntent(Bundle bundle){
        username=bundle.getString("username");
        isAdmin=bundle.getInt("isAdmin");
        point=bundle.getInt("CurrentPoints");

    }
}