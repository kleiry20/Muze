package com.example.muze;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText RegisterPhone, RegisterPassword, RegisterName;
    String phone, password, name;   //to save these details from edit text
    Button RegisterButton;
    ProgressDialog LoadingBar;
    //Loading Bar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterPhone = (EditText) findViewById(R.id.register_phone);
        RegisterPassword = (EditText) findViewById(R.id.register_password);
        RegisterName = (EditText) findViewById(R.id.register_name);

        RegisterButton = (Button) findViewById(R.id.register_btn);

        LoadingBar = new ProgressDialog(this);

        LoadingBar.setTitle("Creating Account..");
        LoadingBar.setMessage("Please wait while we check your credentials..");
        LoadingBar.setCanceledOnTouchOutside(false);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //now on click button we will take text from edit text and validate if it is empty or not
                //save edit text entries in string that we take in starting
                phone = RegisterPhone.getText().toString();
                password = RegisterPassword.getText().toString();
                name = RegisterName.getText().toString();


                CreateNewAccount(phone, password,name);

            }
        });
    }


    private void CreateNewAccount(String phone, String password, String name){
        //now check if it is empty or  not

        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else{
            //start creating account
            LoadingBar.show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    // now check number that user entered is present in our database or not
                    if(!(snapshot.child("Users").child(phone).exists())){
                        //if user doesn't exist then we create account in db
                        HashMap<String,Object> userdata = new HashMap<>();
                        userdata.put("Phone",phone);
                        userdata.put("Password",password);
                        userdata.put("Name",name);

                        mRef.child("Users").child(phone).updateChildren(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                
                                if(task.isSuccessful()){
                                    LoadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    LoadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                                }

                                RegisterPhone.setText(null);
                                RegisterPassword.setText(null);
                                RegisterName.setText(null);
                            }
                        });
                    }
                    else{
                        //if user exists
                        LoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "User with this number already exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
        }



    }
}