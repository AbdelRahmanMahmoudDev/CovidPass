package com.example.covidpassproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUP extends AppCompatActivity {

    //Spinner listView;
   // ArrayList<String> vaccine;
   // ArrayAdapter adapter;
    Button signup;
    HashMap<String, String> form_data;
    String Name;
    String Email;
    String Password;
    String password_check;
    String Phone;
    String vacid;
    String ID;

    StepView stepView;
    List<String> steps=new ArrayList<>();

    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button)findViewById(R.id.SignUp_btn);
        stepView=findViewById(R.id.step_view);

        steps.add("Name and Password");
        steps.add("Email and Phone");
        steps.add("Vaccination and ID");
        stepView.setSteps(steps);


       // vaccine=new ArrayList<String>(Arrays.asList("astrazeneca","fizer"));
       // adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,vaccine);

       // listView.setPrompt("Vaccine Name");

       // listView.setAdapter(adapter);

        form_data = new HashMap<String, String>();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: cross reference password with PasswordCheck
                PersonNode person_node = new PersonNode();

                // Enforce an empty table every time
                if(!form_data.isEmpty()) {
                    form_data.clear();
                }

              /*  String Name=name.getText().toString();
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
                form_data.put(ID, id);*/

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

                /*if(!Password.equals(password_check)) {
                    passwordCheck.setError("Doesn't match password field");
                    return;
                }*/

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

    public void fillNamePassword(String n, String pass)
    {
        form_data.put(Name,n);
        form_data.put(Password,pass);
    }

    public void fillemailPhone(String e,String phone)
    {
        form_data.put(Email,e);
        form_data.put(Phone,phone);
    }
    public void fillvacIDid(String vid,String id)
    {
        form_data.put(vacid,vid);
        form_data.put(ID,id);
    }
}