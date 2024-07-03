package com.example.groceryexpirytrackingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
    Uri imageUri;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ImageView uploadProfileImage;
    TextInputEditText username,password,password1,phoneNumber,fullName;
    Button btn_Register;
    String pw,pnum,fn;
    DatabaseReference reference;
    TextView clcikLoginl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Find view By id
        uploadProfileImage=findViewById(R.id.uploadProfile);
        username=findViewById(R.id.reg_username);
        password=findViewById(R.id.reg_pass);
        password1=findViewById(R.id.reg_pass_1);
        phoneNumber=findViewById(R.id.phoneNum);
        fullName=findViewById(R.id.reg_fullname);
        btn_Register=findViewById(R.id.btn_register);
        clcikLoginl=findViewById(R.id.click_to_login_text);
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //Image handle
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadProfileImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(Register.this, "No Image Select", Toast.LENGTH_SHORT).show();
                        }
                    };
                });
        uploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker=new Intent();
                photopicker.setAction(Intent.ACTION_GET_CONTENT);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });
        //Image handle end
        clcikLoginl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });


        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()==true){
                    //if validation is accepts
                    if (imageUri!=null) {
                        //variable specify in global
                        checkUser();
                    }
                    else {
                        Toast.makeText(Register.this, "Please upload your profile image", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register.this, "Please Check All The Field...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void UploadToFirebase(String username, String phoneNumber,String password, Uri imageUri, int isAdmin,String fullname,int points){
        //specifies image get instances & reference
        StorageReference imageReference=storageReference.child("Users/"+username+"."+getFileExtension(imageUri));
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String ImageURL=uri.toString();
                        UserVer2 i1=new UserVer2(username,phoneNumber,password,ImageURL,isAdmin,fullname,points);
                        reference = FirebaseDatabase.getInstance().getReference("UsersVer2");
                        reference.child(username).setValue(i1);
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

    boolean validation(){
        //Image Validation, Username Validation

        if (username.length()==0){
            username.setError("This field is require");
            return false;
        }
        if (password.length()<8){

            if (password.length()==0){
                password.setError("This field is require");
            }
            else {
                password.setError("Password should be at least 8 charecter");
            }
            return false;
        }
        if (password1.length()==0){
            password1.setError("This field is require");
            return false;
        }
        if (fullName.length()==0){
            fullName.setError("This field is require");
            return false;
        }
        String p1=password.getText().toString();
        String p2=password1.getText().toString();
        if (p1.equals(p2)==false){
            password1.setError("Password is not match");
            return false;
        }
        if (phoneNumber.length()<10 || phoneNumber.length()>11){
            if (phoneNumber.length()<0){
                phoneNumber.setError("Phone number is require");
                return false;
            }
            else{
                phoneNumber.setError("Phone number should be at least 10 and not exceed 11");
            }
        }
        return true;
    }
    void checkUser(){
        String user=username.getText().toString();
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("UsersVer2");
        Query checkUserDatabase=reference1.orderByChild("username").equalTo(user);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            //the method to check and ensure username is unique
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username.setError("Username already exits");
                }
                else{
                    pw = password.getText().toString();
                    pnum=phoneNumber.getText().toString();
                    fn=fullName.getText().toString();
                    UploadToFirebase(user, pnum, pw, imageUri, 0,fn,0); // maybe need to change places
                    Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register.this,Login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Register.this, "Unexpected error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}