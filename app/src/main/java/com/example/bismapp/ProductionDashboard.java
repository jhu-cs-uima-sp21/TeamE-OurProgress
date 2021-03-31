package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductionDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductionDashboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Context cntx;
    //public MainActivity myact;
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


    public ProductionDashboard() {
        // Required empty public constructor
        //ProgressBar id = (ProgressBar)findViewById(R.id.progressBar2;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductionDashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductionDashboard newInstance(String param1, String param2) {
        ProductionDashboard fragment = new ProductionDashboard();
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_production_dashboard, container, false);

        View myView = inflater.inflate(R.layout.fragment_production_dashboard, container, false);
        Context cntx = getActivity().getApplicationContext();

        buttonClick.setDuration(100);
        ImageButton team_btn = (ImageButton) myView.findViewById(R.id.teamButton);
        team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                //TODO: MAKE THIS ON CLICK LEAD BACK TO YOURTEAMS
                getActivity().finish();
                //Intent intent = new Intent(myView.getApplicationContext(), YourTeams.class);
                //startActivity(intent);
            }
        });

        ImageButton settings_btn = (ImageButton) myView.findViewById(R.id.settingsButton);
        team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                //TODO: MAKE THIS ON CLICK LEAD BACK TO EDIT TEAMAS
                //Intent intent = new Intent(myView.getApplicationContext(), YourTeams.class);
                //startActivity(intent);
            }
        });

        return myView;
    }
}