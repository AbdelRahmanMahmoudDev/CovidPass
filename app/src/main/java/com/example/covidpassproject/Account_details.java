package com.example.covidpassproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account_details extends Fragment {
    TextView t1,t2,t3,t4;
    String name,email,phone,vid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account_details.
     */
    // TODO: Rename and change types and number of parameters
    public static Account_details newInstance(String param1, String param2) {
        Account_details fragment = new Account_details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_account_details, container, false);
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        // We don't need this, FireBaseAuth assigns a new UUID for authentication anyway
        // so we can't cross-reference this with anything
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref=database.getReference().child("Person").child(userID);
        t1=v.findViewById(R.id.nt);
        t2=v.findViewById(R.id.et);
        t3=v.findViewById(R.id.pt);
        t4=v.findViewById(R.id.vt);

        ref.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "DatabaseReference";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    name=snapshot.child("name").getValue(String.class);

                    t1.setText(name);
                    email=snapshot.child("email").getValue(String.class);
                    t2.setText(email);
                    phone=snapshot.child("phone").getValue(String.class);
                    t3.setText(phone);
                    vid=snapshot.child("vacID").getValue(String.class);
                    t4.setText(vid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}