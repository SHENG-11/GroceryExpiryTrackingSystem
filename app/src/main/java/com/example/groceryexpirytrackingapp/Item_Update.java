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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Item_Update extends AppCompatActivity {
    Uri imageUri;
    ImageView ItemImage;
    TextInputEditText itemName, numberOfitem;
    TextInputEditText purchase, expire, barcode1;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    String Item_name, Item_exp_date, Item_purchasedate, Item_barcode, Item_ImageUrlOld;
    int NumOfItem;
    DatabaseReference reference,reference2;
    FloatingActionButton delete, update;
    String key = "";
    String imageurl = "";
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
        purchase = findViewById(R.id.item_instock_date1);
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
                    }

                    ;
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
        purchase.setOnClickListener(new View.OnClickListener() {//purchases date can only set before today
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Item_Update.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        purchase.setText(date);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
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
                reference = FirebaseDatabase.getInstance().getReference("ItemList");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference1 = storage.getReferenceFromUrl(Item_ImageUrlOld);
                storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(Item_Update.this, "Item Delete", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        });

    }

    void ActiongetIntent(Bundle bundle) {
        Item_name = getIntent().getStringExtra("Name");
        Item_exp_date = getIntent().getStringExtra("Exp");
        Item_purchasedate = getIntent().getStringExtra("PDate");
        Item_barcode = getIntent().getStringExtra("Barcode");
        NumOfItem = getIntent().getIntExtra("NumItem", 0);//int

        setToPosition();
    }

    void setToPosition() {

        purchase.setText(Item_purchasedate);
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

        if (purchase.length() == 0) {
            purchase.setError("This field is require");
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
        if (imageUri != null) {//imageUri is new uri
            Item_name = itemName.getText().toString();
            StorageReference imageReference = FirebaseStorage.getInstance().getReference("ItemPic/").child(Item_name+"."+getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageurl = uri.toString();
                            updateData();
                        }
                    });
                }
            });
        }
        else{
            imageurl = Item_ImageUrlOld;
            Item_name = itemName.getText().toString();
            StorageReference imageReference = storageReference.child("ItemPic"+Item_name + "." + getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageurl = uri.toString();
                            updateData();
                        }
                    });
                }
            });
        }

        //updateData();
    }

    void updateData() {

        Item_name = itemName.getText().toString();
        Item_exp_date = expire.getText().toString();
        Item_purchasedate = purchase.getText().toString();
        Item_barcode = barcode1.getText().toString();
        NumOfItem = Integer.parseInt(numberOfitem.getText().toString());
        ItemVer1 i1 = new ItemVer1(Item_name, Item_exp_date, Item_purchasedate, Item_barcode, imageurl, NumOfItem);


        reference = FirebaseDatabase.getInstance().getReference("ItemList").child(Item_name);
        reference.setValue(i1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                StorageReference reference1 = FirebaseStorage.getInstance().getReferenceFromUrl(Item_ImageUrlOld);
                reference1.delete();
                reference2=FirebaseDatabase.getInstance().getReference("ItemList");
                reference2.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Item_Update.this, "Update", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Item_Update.this, "Update Fail", Toast.LENGTH_SHORT).show();
                    }
                });
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