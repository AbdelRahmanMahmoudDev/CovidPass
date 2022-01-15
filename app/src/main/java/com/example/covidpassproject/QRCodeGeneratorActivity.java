package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QRCodeGeneratorActivity extends AppCompatActivity {


     ImageView qrImage;
     String inputValue;
     String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
     Bitmap bitmap;
     QRGEncoder qrgEncoder;
     AppCompatActivity activity;

    FirebaseDatabase database=FirebaseDatabase.getInstance("https://covidtest-15516-default-rtdb.firebaseio.com/");
    String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref=database.getReference().child("Person").child(userID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        activity = this;
        qrImage = findViewById(R.id.qr_image);
        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "DatabaseReference";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String id=snapshot.child("id").getValue(String.class);
                    String vid=snapshot.child("vacID").getValue(String.class);



                    inputValue=id.substring(id.length()-4)+vid;
                    if (inputValue.length() > 0) {
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = Math.min(width, height);
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(
                                inputValue, null,
                                QRGContents.Type.TEXT,
                                smallerDimension);
                        qrgEncoder.setColorBlack(Color.BLACK);
                        qrgEncoder.setColorWhite(Color.WHITE);
                        try {
                            bitmap = qrgEncoder.getBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}