package com.example.covidpassproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    Button back,signin;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        back=findViewById(R.id.back_btn);
        signin=findViewById(R.id.signin_btn);
        email=findViewById(R.id.email_txt);
        password=findViewById(R.id.password_txt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,IntroActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Temporary path for testing google maps api
                // startActivity(new Intent(SignIn.this, MapsActivity.class));
                startActivity(new Intent(SignIn.this, MainActivity.class));

                //String Email = email.getText().toString();
                //String Password = password.getText().toString();
//
                //PersonNode node = new PersonNode();
                //node.GetFirebaseAuth().signInWithEmailAndPassword(Email, Password).addnSuccessListener(success -> {
                //    startActivity(new Intent(SignIn.this, MainActivity.class));
                //}).addOnFailureListener(failure -> {
                //    Toast.makeText(SignIn.this, failure.getMessage(), Toast.LENGTH_SHORT);
                //});
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        PersonNode node = new PersonNode();
        FirebaseUser user = node.GetFirebaseAuth().getCurrentUser();
        if(user != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}