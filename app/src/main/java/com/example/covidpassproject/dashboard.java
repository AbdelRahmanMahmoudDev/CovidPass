package com.example.covidpassproject;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard extends Fragment {
    LottieAnimationView animationView;
    WebView webView;
    int i=0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboard newInstance(String param1, String param2) {
        dashboard fragment = new dashboard();
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
        View v =inflater.inflate(R.layout.fragment_dashboard, container, false);
        animationView=v.findViewById(R.id.aView);
        animationView.setAnimation(R.raw.avoid_contacts);
        animationView.playAnimation();
        int[] array ={R.raw.avoid_crowd_places,R.raw.avoid_handshakes,R.raw.avoid_travelling,R.raw.dont_touch_face,R.raw.hand_sanitizer,R.raw.stay_at_home,R.raw.use_a_mask,R.raw.wash_hands,R.raw.avoid_contacts};


        new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                animationView.setAnimation(array[i]);
                animationView.playAnimation();
                i++;
                if(i== array.length)
                    i=0;
                start();
            }
        }.start();

        webView=v.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.worldometers.info/coronavirus/country/egypt/");
        webView.scrollTo(0,500);
        return v;
    }
}