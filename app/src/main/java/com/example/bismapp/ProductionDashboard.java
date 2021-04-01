package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductionDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductionDashboard extends Fragment {
    // TODO: Rename and change types of parameters
    public Context cntx;
    private SharedPreferences myPrefs;
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private static final String TAG = "dbref: ";
    private int daily_goal, units_produced, percent;



    public ProductionDashboard() {

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

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

        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        String teamID = myPrefs.getString("TEAM", "");

        //READ FROM DATABASE TO CHECK IF MANAGER
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                daily_goal = snapshot.child("teams").child(teamID).child("daily_goal").getValue(Integer.class);
                units_produced = snapshot.child("teams").child(teamID).child("units_produced").getValue(Integer.class);
                percent = (int) (((double) units_produced / daily_goal) * 100);
                ProgressBar progressBar = (ProgressBar) myView.findViewById(R.id.circularProgressbar);
                progressBar.setSecondaryProgress(percent);
                TextView per_text = (TextView) myView.findViewById(R.id.textView);
                TextView prod_txt = (TextView) myView.findViewById(R.id.prodText);
                per_text.setText(percent + "%");
                prod_txt.setText(units_produced + " units out of \n" + daily_goal);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





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