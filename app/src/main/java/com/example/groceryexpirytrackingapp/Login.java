package com.example.groceryexpirytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    TextInputEditText un,pw;
    Button btn_login1;
    DatabaseReference databaseReference;
    TextView click_to_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Find view by id
        un=findViewById(R.id.username);
        pw=findViewById(R.id.login_pass);
        btn_login1=findViewById(R.id.btn_login);
        click_to_reg=findViewById(R.id.click_to_register);
        //>>>>>>>>>>>>>>>>>>>>>>>.
        click_to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        btn_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidat()){
                validuser();
                }
                else{
                    Toast.makeText(Login.this, "Please Check all field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    boolean inputValidat(){
        if (un.length()==0){
            un.setError("This field is require");
            return false;
        }
        if (pw.length()==0){
            pw.setError("This field is require");
            return false;
        }
        return true;
    }
    public void validuser(){
        String user,password;
        user=un.getText().toString();
        password=pw.getText().toString();
        databaseReference= FirebaseDatabase.getInstance().getReference("UsersVer2");
        Query q1=databaseReference.orderByChild("username").equalTo(user);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    final String userPwInDB=snapshot.child(user).child("password").getValue(String.class);

                    if (userPwInDB.equals(password)){
                        //If password match
                        //Create intent to homepage
                        int isAdmin=snapshot.child(user).child("isAdmin").getValue(Integer.class);
                        if (isAdmin==1){
                            Intent intent = new Intent(Login.this, UserProfile.class);
                            intent.putExtra("username",user);
                            intent.putExtra("isAdmin",isAdmin);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(Login.this, UserProfile.class);
                            intent.putExtra("username",user);
                            intent.putExtra("isAdmin",isAdmin);
                            startActivity(intent);
                        }

                        Toast.makeText(Login.this, "Successful Login", Toast.LENGTH_SHORT).show();

                    }else{
                        pw.setError("Invalid Credentials");
                        pw.requestFocus();
                    }
                }else {
                    un.setError("User does not exits");
                    un.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}