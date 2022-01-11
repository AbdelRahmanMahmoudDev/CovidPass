package com.example.covidpassproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Email_Password_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Email_Password_fragment extends Fragment {
    EditText name,pass,passcheck;
    Button n1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Email_Password_fragment() {
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
    public static Email_Password_fragment newInstance(String param1, String param2) {
        Email_Password_fragment fragment = new Email_Password_fragment();
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
        View v=inflater.inflate(R.layout.fragment_email__password_fragment, container, false);
        name=v.findViewById(R.id.frgName_txt);
        pass=v.findViewById(R.id.password_txt);
        passcheck=v.findViewById(R.id.frgpasswordCheck_txt);
        n1=v.findViewById(R.id.nv1_tn);
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(name.getText().toString()==null||pass.getText().toString()==null||passcheck.getText().toString()==null)
                {
                    Toast.makeText(getActivity(),"Please fill all the Data First",Toast.LENGTH_SHORT).show();
                }
                else if(pass.getText().toString()!=passcheck.getText().toString())
                {
                    Toast.makeText(getActivity(),"Doesn't match password field",Toast.LENGTH_SHORT).show();
                }
                else
                {*/
                    String n=name.getText().toString();
                    String p=pass.getText().toString();
                    SignUP s=(SignUP) getActivity();
                    s.fillNamePassword(n,p);
                    s.stepView.go(1,true);
                    Navigation.findNavController(v).navigate(R.id.action_name_password_fragment_to_email_Password_fragment);


               // }
            }
        });
        return v;
    }
}