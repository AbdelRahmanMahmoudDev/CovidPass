package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserTypeSelectorActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    // We don't need this, FireBaseAuth assigns a new UUID for authentication anyway
    // so we can't cross-reference this with anything
    String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference ref=database.getReference().child("Person").child(userID);

    String job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selector);

        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "DatabaseReference";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    job=snapshot.child("job").getValue(String.class);

                    if(job.isEmpty())
                    {
                        Intent i=new Intent(UserTypeSelectorActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i=new Intent(UserTypeSelectorActivity.this,QRCodeScanner.class);
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
