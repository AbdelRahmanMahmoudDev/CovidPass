package com.example.covidpassproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;

public class SignUP extends AppCompatActivity {
    EditText email,password,passwordCheck,name,id,vaccineCode,phone;
    CheckBox Vaccinated;
    Button signup,back;
    ListView listView;
    ArrayList<String> vaccine;
    ArrayAdapter adapter;
    private String UserID;


    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.email_txt);
        phone=findViewById(R.id.Phone_txt);
        password=findViewById(R.id.password_txt);
        passwordCheck=findViewById(R.id.PasswordCheck_txt);
        name =findViewById(R.id.name_txt);
        id=findViewById(R.id.id_txt);
        vaccineCode=findViewById(R.id.VaccineNum_txt);
        listView=findViewById(R.id.VaccineList);
        Vaccinated=findViewById(R.id.checkBox);
        signup=findViewById(R.id.SignUp_btn);
        back=findViewById(R.id.back_btn);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        FirebaseUser user=auth.getCurrentUser();
       UserID = user.getUid();


        vaccine=new ArrayList<String>(Arrays.asList("astrazeneca","fizer"));
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,vaccine);

        listView.setAdapter(adapter);


        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Toast.makeText(SignUP.this,"Successfully Signed In With:"+user.getEmail(),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUP.this,"Successfully Signed Out",Toast.LENGTH_SHORT).show();
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=name.getText().toString();
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                String PasswordCheck=passwordCheck.getText().toString();
                String Phone=phone.getText().toString();
                String vacid=vaccineCode.getText().toString();
                String ID=id.getText().toString();

                Person p =new Person(Name,Email,Phone,vacid,ID,Password);
                myRef.child("users").child(UserID).setValue(p);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            auth.removeAuthStateListener(mAuthListener);
        }
    }
}