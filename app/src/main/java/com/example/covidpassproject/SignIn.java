package com.example.covidpassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent intent=new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=new Intent(SignIn.this,vaccinated_Activity.class);
               // if(email.getText().toString()=="1"&&password.getText().toString()=="1")
                startActivity(signin);

                //Intent signin2=new Intent(SignIn.this,NotVaccinated_Activity.class);
                //if(email.getText().toString()=="2"&&password.getText().toString()=="2")
                  //  startActivity(signin2);
            }
        });

    }
}