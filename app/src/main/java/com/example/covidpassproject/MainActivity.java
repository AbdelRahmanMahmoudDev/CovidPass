package com.example.covidpassproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.covidpassproject.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView namemenu,emailmenu,signout;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    // We don't need this, FireBaseAuth assigns a new UUID for authentication anyway
    // so we can't cross-reference this with anything
   String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference ref=database.getReference().child("Person").child(userID);


    String name,email;


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,QRCodeGeneratorActivity.class);
                startActivity(i);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_maps, R.id.nav_details)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView=navigationView.getHeaderView(0);
        namemenu=(TextView) headerView.findViewById(R.id.nameMenu);
        emailmenu=(TextView) headerView.findViewById(R.id.emailMenu);
        signout=(TextView) headerView.findViewById(R.id.Signoutt);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MainActivity.this,SignIn.class));
            }
        });



        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "DatabaseReference";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                             name=snapshot.child("name").getValue(String.class);

                            namemenu.setText(name);
                            email=snapshot.child("email").getValue(String.class);
                            emailmenu.setText(email);

                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}