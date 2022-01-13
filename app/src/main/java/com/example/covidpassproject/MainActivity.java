package com.example.covidpassproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
   TextView namet,emailt,vacsatatt,phonet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namet=(TextView) findViewById(R.id.txtname);
        emailt=(TextView) findViewById(R.id.txtemail);
        vacsatatt=(TextView) findViewById(R.id.txtvacstat);
        phonet=(TextView) findViewById(R.id.txtphone);



    }
}