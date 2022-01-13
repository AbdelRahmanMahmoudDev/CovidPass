package com.example.covidpassproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link name_password_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class name_password_fragment extends Fragment {
    EditText name,pass,passcheck;
    Button n1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public name_password_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment name_password_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static name_password_fragment newInstance(String param1, String param2) {
        name_password_fragment fragment = new name_password_fragment();
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

        View v = inflater.inflate(R.layout.fragment_name_password_fragment, container, false);
        Animation btnAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.button_animation);


        name=v.findViewById(R.id.frgName_txt);
        pass=v.findViewById(R.id.frgPassword_txt);
        passcheck=v.findViewById(R.id.frgpasswordCheck_txt);
        n1=v.findViewById(R.id.nv1_tn);

            n1.setVisibility(View.VISIBLE);
            n1.setAnimation(btnAnim);



        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()&&pass.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter name and password first",Toast.LENGTH_SHORT).show();
                }
                else if(!name.getText().toString().isEmpty()&&pass.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Enter a valid password ",Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().isEmpty()&&!pass.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Enter your name",Toast.LENGTH_SHORT).show();
                }
                else if(!pass.getText().toString().equals(passcheck.getText().toString()))
                {
                    Toast.makeText(getActivity(),"Doesn't match password field",Toast.LENGTH_SHORT).show();
                }
                else {
                    String n = name.getText().toString();
                    String p = pass.getText().toString();
                    SignUP s = (SignUP) getActivity();
                    s.fillNamePassword(n, p);
                    s.stepView.go(1, true);
                    Navigation.findNavController(v).navigate(R.id.action_name_password_fragment_to_email_Password_fragment);
                }


            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}