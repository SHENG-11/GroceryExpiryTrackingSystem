package com.example.groceryexpirytrackingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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

import com.example.groceryexpirytrackingapp.databinding.ActivityMainBinding;
import com.example.groceryexpirytrackingapp.databinding.ActivityUserProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    FloatingActionButton fat;
    List<ItemVer1> itemVer1s;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SearchView searchView;
    ItemAdapter itemAdapter;
    ImageView scanBarcode;
    String username;
    int isAdmin,point;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FInd view by Id
        recyclerView=findViewById(R.id.recycleView_item);
        fat=findViewById(R.id.btn_floating_add);
        searchView=findViewById(R.id.search1);
        searchView.clearFocus();
        scanBarcode=findViewById(R.id.scanBarCode1);
        //Swipe Refresh Layout
        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        //Bundle get intent
        Bundle bundle=getIntent().getExtras();
        StartgetIntent(bundle);
        //Bottom Navigation set
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.bottom_home){
                return true;
            } else if (item.getItemId()==R.id.bottom_mission) {
                Intent intent=new Intent(MainActivity.this, Admin_Mission.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_done) {
                Intent intent=new Intent(MainActivity.this, Admin_Mission_PendingList.class);
                intent.putExtra("username",username);
                intent.putExtra("isAdmin",isAdmin);
                intent.putExtra("CurrentPoints",point);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                startActivity(intent);
                return true;
            }else if (item.getItemId()==R.id.bottom_profile) {
                Intent intent=new Intent(MainActivity.this, UserProfile.class);
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
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

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
                    item1.setKey(itemSnapshot.getKey());
                    itemVer1s.add(item1);
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Pending
                searchList(newText);
                return true;
            }
        });


    }
    public void searchList(String text){
        ArrayList<ItemVer1> searachList1=new ArrayList<>();
        for (ItemVer1 ittem:itemVer1s){
            //search by title
            if (ittem.getName().toLowerCase().contains(text.toLowerCase()) || ittem.getBarcode().toLowerCase().contains(text.toLowerCase())){
                searachList1.add(ittem);

            }
        }
        itemAdapter.searchItemInformation(searachList1);
    }
    //Scan barcode
    private void scanCode(){
        ScanOptions options=new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher=registerForActivityResult(new ScanContract(), result->{
        if (result.getContents()!=null){
            String barcode=result.getContents();
            searchView.setQuery(barcode,true);
        }
    });
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>
    void StartgetIntent(Bundle bundle){
        username=bundle.getString("username");
        isAdmin=bundle.getInt("isAdmin");
        point=bundle.getInt("CurrentPoints");

    }
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }
}