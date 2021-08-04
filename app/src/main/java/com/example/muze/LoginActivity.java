package com.example.muze;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {


    EditText LoginPhone, LoginPassword;
    Button LoginButton;
    String phone, password; //to store phone and password that user entered in edittext
    String userPassword;    //to store actual password from database

    ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoadingBar = new ProgressDialog(this);
        LoginPhone = (EditText) findViewById(R.id.login_phone);
        LoginPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_btn);

        LoadingBar.setTitle("Login Account");
        LoadingBar.setMessage("Please wait while we check in our database");
        LoadingBar.setCanceledOnTouchOutside(false);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(LoginActivity.this, UserActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {


            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    phone = LoginPhone.getText().toString();
                    password = LoginPassword.getText().toString();
                    LoginAccount(phone, password);
                }
            });
        }
    }

    private void LoginAccount(String phone, String password) {

       if (TextUtils.isEmpty(phone)){
           Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
       }
        else if(TextUtils.isEmpty(password)){
           Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
       }
        else{
            LoadingBar.show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    //now if edit text is not empty we check if phone entered by user exists in the db or not
                    // if it doesnt exist then we retrieve pass in "userPassword" and then
                    //match that pass with the pass that user entered in edittext
                    //if it matches then we redirect user to home activity
                    if(snapshot.child("Users").child(phone).exists()){
                        //if exist retrieve password from db
                        userPassword = snapshot.child("Users").child(phone).child("Password").getValue().toString();

                        //now check
                        if(password.equals(userPassword)){
                            //then goto song list activity
                            LoadingBar.dismiss();
                            Intent i = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(i);

                            // so if user exists with the number that they enter and with correct password then user will redirect to
                            //song list activity

                        }
                        else{
                                //if user exists but password is not correct
                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Please enter the correct password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //user does not exist
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
       }
    }
}