package com.example.groceryexpirytrackingapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Item_Update extends AppCompatActivity {
    Uri imageUri;
    ImageView ItemImage;
    ValueEventListener valueEventListener;
    TextInputEditText itemName, numberOfitem;
    TextInputEditText  expire, barcode1;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    String Item_name, Item_exp_date, Item_barcode, Item_ImageUrlOld,oldItemName;
    int NumOfItem;
    DatabaseReference reference,reference2,reference3;
    FloatingActionButton delete, update;
    String key = "";
    String imageurl;
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String barcode = result.getContents();
            barcode1.setText(barcode);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);

        //Find view by id

        expire = findViewById(R.id.item_expire_date1);
        barcode1 = findViewById(R.id.item_barcode1);
        itemName = findViewById(R.id.item_name1);
        numberOfitem = findViewById(R.id.numItem1);
        delete = findViewById(R.id.btn_delete_item);
        update = findViewById(R.id.btn_update_item);
        ItemImage = findViewById(R.id.item_pic1);
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //Get Intent from previous pages
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ActiongetIntent(bundle);
            key = bundle.getString("key");
            Item_ImageUrlOld = bundle.getString("Image");
            reference3=FirebaseDatabase.getInstance().getReference().child("ItemList");
            //databaseReference.addValueEventListener(new ValueEventListener()
            valueEventListener = reference3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     oldItemName = dataSnapshot.child(key).child("name").getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            });
        }
        //Calender details
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

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
                            Toast.makeText(Item_Update.this, "No Image Select", Toast.LENGTH_SHORT).show();
                        }
                    };
                });
        ItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });
        //Image handle end here

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    SaveData();

                } else
                    Toast.makeText(Item_Update.this, "please check all field", Toast.LENGTH_SHORT).show();
            }
        });
        barcode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }//press the edit text field to scan item barcode and set it into column
        });

        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Item_Update.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String expdate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        expire.setText(expdate);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Item_Update.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress);
                AlertDialog dialog = builder.create();
                dialog.show();

                reference = FirebaseDatabase.getInstance().getReference("ItemList");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference1 = storage.getReferenceFromUrl(Item_ImageUrlOld);
                storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(Item_Update.this, "Item Delete", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Item_Update.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        
                    }
                });
            }
        });

    }

    void ActiongetIntent(Bundle bundle) {
        Item_name = getIntent().getStringExtra("Name");
        Item_exp_date = getIntent().getStringExtra("Exp");

        Item_barcode = getIntent().getStringExtra("Barcode");
        NumOfItem = getIntent().getIntExtra("NumItem", 0);//int

        setToPosition();
    }

    void setToPosition() {

        expire.setText(Item_exp_date);
        barcode1.setText(Item_barcode);
        itemName.setText(Item_name);
        numberOfitem.setText(String.valueOf(NumOfItem));
        storageReference = FirebaseStorage.getInstance().getReference("ItemPic/" + Item_name + ".jpg");
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    ItemImage.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    boolean validation() {
        //Image Validation, Username Validation
        if (itemName.length() == 0) {
            itemName.setError("This field is require");
            return false;
        }
        if (numberOfitem.length() == 0) {
            numberOfitem.setError("This field is require");
            return false;
        }
        if (barcode1.length() == 0) {
            barcode1.setError("This field is require");
            return false;
        }
        String currentData=expire.getText().toString();
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(currentData);//convert current ans to date format
            if (!currentData.equals(sdf.format(date))) {
                date = null;// if format was wrong
            }
        } catch (ParseException e) {
            expire.setError("Please ensure correct date format");
            return false;
        }

        if (expire.length() == 0) {
            expire.setError("This field is require");
            return false;
        }

        return true;
    }

    void SaveData() {
        //specifies image get instances & reference
        if (imageUri != null) {//new image set
            Item_name = itemName.getText().toString();
            //alert dialog box setup
            AlertDialog.Builder builder = new AlertDialog.Builder(Item_Update.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress);
            AlertDialog dialog = builder.create();
            dialog.show();
            //annimation show
            StorageReference imageReference = FirebaseStorage.getInstance().getReference("ItemPic/").child(Item_name+"."+getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // in this stage, new image is successfully add and call next functions
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageurl = uri.toString();//uri convert to string and then save into image url
                            updateData(); // with new image url
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        else {
            // old image used, meaning that, not need to update images
            Toast.makeText(this, "Please choose new images", Toast.LENGTH_SHORT).show();
        }

    }


    void updateData() {
        Item_name = itemName.getText().toString();
        Item_exp_date = expire.getText().toString();
        Item_barcode = barcode1.getText().toString();
        NumOfItem = Integer.parseInt(numberOfitem.getText().toString());
        ItemVer1 i1 = new ItemVer1(Item_name, Item_exp_date, Item_barcode, imageurl, NumOfItem);

        reference = FirebaseDatabase.getInstance().getReference("ItemList").child(Item_name);//new name
        reference.setValue(i1).addOnCompleteListener(new OnCompleteListener<Void>() {
            //set new value into child
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                StorageReference reference1 = FirebaseStorage.getInstance().getReferenceFromUrl(Item_ImageUrlOld);

                if (Item_name.toLowerCase().equals(oldItemName.toLowerCase())){
                    Toast.makeText(Item_Update.this, "Update", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    reference2=FirebaseDatabase.getInstance().getReference("ItemList");
                    reference1.delete(); // delete old image
                    reference2.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {//because got remove old one, so if same name will be remove
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Item_Update.this, "Update", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Item_Update.this, "Update Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Item_Update.this, "Fail Update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String getFileExtension(Uri fileUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(fileUri));
    }
}