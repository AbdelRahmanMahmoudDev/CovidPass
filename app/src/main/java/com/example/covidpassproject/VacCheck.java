package com.example.covidpassproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VacCheck#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VacCheck extends Fragment {
    LottieAnimationView vac,notvac;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VacCheck() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VacCheck.
     */
    // TODO: Rename and change types and number of parameters
    public static VacCheck newInstance(String param1, String param2) {
        VacCheck fragment = new VacCheck();
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
        View v =inflater.inflate(R.layout.fragment_vac_check, container, false);


        vac=(LottieAnimationView) v.findViewById(R.id.Vac);
        notvac=(LottieAnimationView) v.findViewById(R.id.notVac);
        vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String VacStatus="Vaccinated";
                SignUP s=(SignUP) getActivity();
                s.fillvac(VacStatus);
                s.stepView.go(3,true);
                Navigation.findNavController(v).navigate(R.id.action_vacCheck_to_id_vacid_fragment);


            }
        });

        notvac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String VacStatus="NotVaccinated";

                SignUP s=(SignUP) getActivity();
                s.fillvac(VacStatus);
                s.stepView.go(3,true);
                Navigation.findNavController(v).navigate(R.id.action_vacCheck_to_id_vacid_fragment);
            }
        });
        return v;
    }
}