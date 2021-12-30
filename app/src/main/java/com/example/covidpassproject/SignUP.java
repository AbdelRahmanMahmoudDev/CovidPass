package com.example.covidpassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static java.util.Arrays.asList;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUP extends AppCompatActivity {
    EditText email,password,passwordCheck,name,id,vaccineCode,phone;
    CheckBox Vaccinated;
    Button signup,back;
    ListView listView;
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    ArrayList<String> vaccine;
    ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.email_txt);
        phone=findViewById(R.id.Phone_txt);
        password=findViewById(R.id.password_txt);
        passwordCheck=findViewById(R.id.PasswordCheck_txt);
        name=findViewById(R.id.name_txt);
        id=findViewById(R.id.id_txt);
        vaccineCode=findViewById(R.id.VaccineNum_txt);
        listView=findViewById(R.id.VaccineList);
        Vaccinated=findViewById(R.id.checkBox);
        signup=findViewById(R.id.SignUp_btn);
        back=findViewById(R.id.back_btn);


        vaccine=new ArrayList<String>(Arrays.asList("astrazeneca","fizer"));
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,vaccine);

        listView.setAdapter(adapter);






    }
}