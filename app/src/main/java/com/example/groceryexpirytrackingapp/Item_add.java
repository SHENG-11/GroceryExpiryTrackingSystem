package com.example.groceryexpirytrackingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;

public class Item_add extends AppCompatActivity {
    Uri imageUri;
    ImageView ItemImage;
    TextInputEditText purchase,expire,barcode1;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    TextInputEditText itemName,numberOfitem;
    String cate_selected;
    Button addItem;
    String Item_name,Item_exp_date,Item_purchasedate,Item_barcode,Item_ImageUrl;
    int NumOfItem;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        //Find view by id

        purchase=findViewById(R.id.item_instock_date);
        expire=findViewById(R.id.item_expire_date);
        barcode1=findViewById(R.id.item_barcode);
        itemName=findViewById(R.id.item_name);
        numberOfitem=findViewById(R.id.numItem);
        addItem=findViewById(R.id.btn_add_item);
        ItemImage=findViewById(R.id.userPicture1);

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

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    checkItemUnique();

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
        if(itemName.length()==0){
            itemName.setError("This field is require");
            return false;
        }
        if (numberOfitem.length()==0){
            numberOfitem.setError("This field is require");
            return false;
        }
        if (cate_selected.length()==0){
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (barcode1.length()==0){
            barcode1.setError("This field is require");
            return false;
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
    void checkItemUnique(){
        String barcod=barcode1.getText().toString();

        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("ItemList");
        Query checkUserDatabase=reference1.orderByChild("barcode").equalTo(barcod);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    barcode1.setError("Barcode is already exits");
                }
                else{
                    //Item_name,Item_exp_date,Item_purchasedate,Item_barcode,Item_Categories;
                    Item_name = itemName.getText().toString();
                    Item_exp_date=expire.getText().toString();
                    Item_purchasedate=purchase.getText().toString();
                    Item_barcode = barcode1.getText().toString();
                    NumOfItem=Integer.parseInt(numberOfitem.getText().toString());
                    UploadToFirebase(Item_name, Item_exp_date, Item_purchasedate,Item_barcode, imageUri,NumOfItem); // maybe need to change places
                    Toast.makeText(Item_add.this, "Upload successful", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Item_add.this, "Unexpected error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void UploadToFirebase(String Item_name, String Item_exp_date,String Item_purchasedate, String Item_barcode, Uri imageUri,int NumOfItem){
        //specifies image get instances & reference

        StorageReference imageReference=storageReference.child("ItemPic/"+Item_name+"."+getFileExtension(imageUri));
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Item_ImageUrl=uri.toString();
                        ItemVer1 i1=new ItemVer1(Item_name,Item_exp_date,Item_purchasedate,Item_barcode,Item_ImageUrl,NumOfItem);
                        reference = FirebaseDatabase.getInstance().getReference("ItemList");
                        reference.child(Item_name).setValue(i1);
                    }
                });
            }
        });
    }
    String getFileExtension(Uri fileUri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(fileUri));
    }

}