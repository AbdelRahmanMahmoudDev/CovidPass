package com.example.covidpassproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link id_vacid_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class id_vacid_fragment extends Fragment {
    TextView t1;
    EditText id,vacid;
    Button signup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public id_vacid_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment id_vacid_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static id_vacid_fragment newInstance(String param1, String param2) {
        id_vacid_fragment fragment = new id_vacid_fragment();
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
        View v=inflater.inflate(R.layout.fragment_id_vacid_fragment, container, false);
        t1=(TextView) v.findViewById(R.id.click);
        id=(EditText)v.findViewById(R.id.ID_txt);
        signup=(Button) v.findViewById(R.id.signup);
        vacid=(EditText)v.findViewById(R.id.VacID_txt);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://egcovac.mohp.gov.eg/#/registration";
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id.getText().toString().isEmpty()&&vacid.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please Enter your ID and Vaccination ID first", Toast.LENGTH_SHORT).show();
                }
                else if (!id.getText().toString().isEmpty()&&vacid.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please Enter your Vaccination ID first", Toast.LENGTH_SHORT).show();
                }
                else if (id.getText().toString().isEmpty()&&!vacid.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please Enter your ID first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String i=id.getText().toString();
                    String vi=vacid.getText().toString();
                    SignUP s=(SignUP) getActivity();
                    s.fillvacIDid(i,vi);
                    s.signup();
                }
            }
        });

        return v;
    }
}