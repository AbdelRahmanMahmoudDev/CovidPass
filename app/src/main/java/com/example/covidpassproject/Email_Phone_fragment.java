package com.example.covidpassproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Email_Phone_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Email_Phone_fragment extends Fragment {
    EditText frgemail,frgphone;
    Button epNext,epBack;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Email_Phone_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Email_Password_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Email_Phone_fragment newInstance(String param1, String param2) {
        Email_Phone_fragment fragment = new Email_Phone_fragment();
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

        View v=inflater.inflate(R.layout.fragment_email__phone_fragment, container, false);
        frgemail=v.findViewById(R.id.frgEmail_txt);
        frgphone=v.findViewById(R.id.frgPhone_txt);
        epNext=v.findViewById(R.id.EPnext_btn);
        epBack=v.findViewById(R.id.EPback_btn);

        epNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frgemail.getText().toString().isEmpty()==true||frgphone.getText().toString().isEmpty()==true)
                {
                    Toast.makeText(getActivity(),"Please Enter a valid email and password first",Toast.LENGTH_SHORT).show();
                }
                else{
                    String e=frgemail.getText().toString();
                    String p=frgphone.getText().toString();
                    SignUP s=(SignUP) getActivity();
                    s.fillemailPhone(e,p);
                    s.stepView.go(2,true);
                    Navigation.findNavController(v).navigate(R.id.action_email_Phone_fragment_to_vacCheck);


                }
            }
        });
        epBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUP s=(SignUP) getActivity();
                s.stepView.done(false);
                s.stepView.go(1,true);

            }
        });


        return v;
    }
}