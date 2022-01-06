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
    private PersonNode person_node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.Email_txt);
        phone=findViewById(R.id.Phone_txt);
        password=findViewById(R.id.Password_txt );
        passwordCheck=findViewById(R.id.PasswordCheck_txt);
        name =findViewById(R.id.name_txt);
        id=findViewById(R.id.id_txt);
        vaccineCode=findViewById(R.id.VaccineNum_txt);
        listView=findViewById(R.id.VaccineList);
        Vaccinated=findViewById(R.id.checkBox);
        signup=findViewById(R.id.SignUp_btn);
        back=findViewById(R.id.back_btn);

        vaccine=new ArrayList<String>(Arrays.asList("astrazeneca","fizer"));
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,vaccine);

        listView.setAdapter(adapter);

        person_node = new PersonNode();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: cross reference password with PasswordCheck
                String Name=name.getText().toString();
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                String PasswordCheck=passwordCheck.getText().toString();
                String Phone=phone.getText().toString();
                String vacid=vaccineCode.getText().toString();
                String ID=id.getText().toString();

                Person p =new Person(Name,Email,Phone,vacid,ID,Password);
                person_node.add(p).addOnFailureListener(failure -> {
                    Toast.makeText(v.getContext(), failure.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });



    }
}