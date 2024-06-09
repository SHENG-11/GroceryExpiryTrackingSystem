package com.example.groceryexpirytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class Item_add extends AppCompatActivity {
    AutoCompleteTextView itemCategories;
    ArrayAdapter<String> categoriesItem;
    String[] item={"Frozen Food","Snacks","Candy"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        //Find view by id
        itemCategories=findViewById(R.id.dropDownCategories);
        categoriesItem=new ArrayAdapter<String>(this,R.layout.itemcategorieslist,item);//layout in middle contain textview
        itemCategories.setAdapter(categoriesItem);
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        itemCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cate_selected=parent.getItemAtPosition(position).toString();// Drop down list item save in this variable
            }
        });
    }
}