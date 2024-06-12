package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.groceryexpirytrackingapp.databinding.ActivityMainBinding;
import com.example.groceryexpirytrackingapp.databinding.ActivityUserProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fat;
    List<ItemVer1> itemVer1s;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SearchView searchView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FInd view by Id
        recyclerView=findViewById(R.id.recycleView_item);
        fat=findViewById(R.id.btn_floating_add);
        searchView=findViewById(R.id.search1);
        //>>>>>>>>>>>>>>>>>>>>>>>>

        //Floating Action Btn add function
        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Item_add.class);
                startActivity(intent);
            }
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //RecycleView Setup
        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //ArrayList Declear
        itemVer1s=new ArrayList<>();
        //the adapter we create
        itemAdapter=new ItemAdapter(MainActivity.this,itemVer1s);
        recyclerView.setAdapter(itemAdapter); // set it into recycleview
        databaseReference= FirebaseDatabase.getInstance().getReference("ItemList");
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemVer1s.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    ItemVer1 item1=itemSnapshot.getValue(ItemVer1.class);
                    itemVer1s.add(item1);
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    }
}