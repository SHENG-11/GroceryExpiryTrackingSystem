package com.example.groceryexpirytrackingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;

public class Item_add extends AppCompatActivity {
    Uri imageUri;
    ImageView ItemImage;
    AutoCompleteTextView itemCategories; //drop down btn
    ArrayAdapter<String> categoriesItem; //Array adapter
    String[] item={"Frozen Food","Snacks","Medical"};//This string show item categories
    TextInputEditText purchase,expire,barcode1;
    TextInputEditText itemName,numberOfitem,catItem;
    String cate_selected;
    Button addItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        //Find view by id
        itemCategories=findViewById(R.id.dropDownCategories);
        categoriesItem=new ArrayAdapter<String>(this,R.layout.itemcategorieslist,item);//layout in middle contain textview
        itemCategories.setAdapter(categoriesItem);
        purchase=findViewById(R.id.item_instock_date);
        expire=findViewById(R.id.item_expire_date);
        barcode1=findViewById(R.id.item_barcode);
        itemName=findViewById(R.id.item_name);
        numberOfitem=findViewById(R.id.numItem);
        addItem=findViewById(R.id.btn_add_item);
        ItemImage=findViewById(R.id.userPicture1);
        //catItem=findViewById(R.id.categoryOfItem);
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //Calender details
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        //Image handle
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            ItemImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(Item_add.this, "No Image Select", Toast.LENGTH_SHORT).show();
                        }
                    };
                });
        ItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker=new Intent();
                photopicker.setAction(Intent.ACTION_GET_CONTENT);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });
        //Image handle end here
        itemCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {//drop down list
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             cate_selected=parent.getItemAtPosition(position).toString();// Drop down list item save in this variable
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    Toast.makeText(Item_add.this, "All item verified", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(Item_add.this, "please check all field", Toast.LENGTH_SHORT).show();
            }
        });
        barcode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }//press the edit text field to scan item barcode and set it into column
        });
        purchase.setOnClickListener(new View.OnClickListener() {//purchases date can only set before today
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog=new DatePickerDialog(Item_add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date=dayOfMonth+"/"+(month+1)+"/"+year;
                        purchase.setText(date);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog=new DatePickerDialog(Item_add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String expdate=dayOfMonth+"/"+(month+1)+"/"+year;
                        expire.setText(expdate);
                    }
                },year,month,day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
        });
    }

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
            barcode1.setText(barcode);
        }
    });

    boolean validation(){
        //Image Validation, Username Validation

        if (numberOfitem.length()==0){
            numberOfitem.setError("This field is require");
            return false;
        }
        
        if (cate_selected.length()==0){
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
        }

         
        if (barcode1.length()==0){
            barcode1.setError("This field is require");
        }

        if (purchase.length()==0){
            purchase.setError("This field is require");
            return false;
        }
        if (expire.length()==0){
            expire.setError("This field is require");
            return false;
        }

        return true;
    }
}