package com.example.covidpassproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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
                Intent intent=new Intent(SignIn.this,QRCodeScanner.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Temporary path for testing google maps api
                // startActivity(new Intent(SignIn.this, MapsActivity.class));


                String Email = email.getText().toString();
                String Password = password.getText().toString();

                PersonNode node = new PersonNode();

                node.GetFirebaseAuth().signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String uuid = user.getUid();
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference ref=database.getReference().child("Scanner").child(uuid);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String bing = snapshot.getKey();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                        }
                        else {
                            Toast.makeText(SignIn.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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