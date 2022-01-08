package com.example.covidpassproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUP extends AppCompatActivity {
    EditText email,password,passwordCheck,name,id,vaccineCode,phone;
    CheckBox Vaccinated;
    Button signup,back;
    Spinner listView;
    ArrayList<String> vaccine;
    ArrayAdapter adapter;
    HashMap<String, EditText> form_data;

    private String UserID;

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

        listView.setPrompt("Vaccine Name");

        listView.setAdapter(adapter);

        form_data = new HashMap<String, EditText>();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: cross reference password with PasswordCheck
                PersonNode person_node = new PersonNode();

                // Enforce an empty table every time
                if(!form_data.isEmpty()) {
                    form_data.clear();
                }

                String Name=name.getText().toString();
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                String password_check=passwordCheck.getText().toString();
                String Phone=phone.getText().toString();
                String vacid=vaccineCode.getText().toString();
                String ID=id.getText().toString();

                form_data.put(Name, name);
                form_data.put(Email,email);
                form_data.put(Password, password);
                form_data.put(password_check, passwordCheck);
                form_data.put(Phone, phone);
                form_data.put(vacid, vaccineCode);
                form_data.put(ID, id);

                // Loop through all fields to get all errors
                boolean something_is_empty = false;
                for(Map.Entry m : form_data.entrySet()) {
                        if(m.getKey().toString().isEmpty()) {
                            EditText empty = (EditText) m.getValue();
                            empty.setError("This field is required");
                            something_is_empty = true;
                    }
                }

                if(something_is_empty) {
                    return;
                }

                if(!Password.equals(password_check)) {
                    passwordCheck.setError("Doesn't match password field");
                    return;
                }

                AtomicBoolean is_added_to_database = new AtomicBoolean(true);
                Person p =new Person(Name,Email,Phone,vacid,ID,Password);
                person_node.add(p).addOnFailureListener(failure -> {
                            is_added_to_database.set(false);
                            Toast.makeText(SignUP.this, failure.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                if(is_added_to_database.get() == true) {

                    person_node.GetFirebaseAuth().createUserWithEmailAndPassword(p.getEmail(), p.getPassword()).addOnSuccessListener(auth -> {

                        Toast.makeText(SignUP.this, "User Created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUP.this, SignIn.class));
                    }).addOnFailureListener(fail -> {
                        Toast.makeText(SignUP.this, fail.getMessage(), Toast.LENGTH_SHORT).show();

                    });
                }
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