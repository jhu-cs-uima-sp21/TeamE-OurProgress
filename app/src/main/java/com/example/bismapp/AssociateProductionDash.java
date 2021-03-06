package com.example.bismapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class AssociateProductionDash extends Fragment {
    private SharedPreferences myPrefs;
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private static final String TAG = "dbref: ";
    private int daily_goal, units_produced, percent;
    private View myView;
    private AssociateNavigationActivity assocNav;

    public AssociateProductionDash() {

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
    public static AssociateProductionDash newInstance(String param1, String param2) {
        AssociateProductionDash fragment = new AssociateProductionDash();
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
        assocNav = (AssociateNavigationActivity) getActivity();
        Context cntx = assocNav.getApplicationContext();

        buttonClick.setDuration(100);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        String teamID = myPrefs.getString("TEAM", "N/A");

        if (teamID.equals("N/A")) {
            myView = inflater.inflate(R.layout.fragment_associate_prod_dash_no_team, container, false);
        } else {
            myView = inflater.inflate(R.layout.fragment_associate_production_dash, container, false);

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

                    Drawable draw;
                    if (percent < 10) {
                        draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_red);
                    } else if (percent < 24) {
                        draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_orange);
                    } else if (percent < 75) {
                        draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_yellow);
                    } else {
                        draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_green);
                    }

                    progressBar.setProgressDrawable(draw);
                    progressBar.setSecondaryProgress(percent);
                    progressBar.setProgress(percent);
                    TextView per_text = (TextView) myView.findViewById(R.id.textView);
                    TextView prod_txt = (TextView) myView.findViewById(R.id.prodText);
                    per_text.setText(percent + "%");
                    if (percent >= 100) {
                        progressBar.setSecondaryProgress(100);
                        progressBar.setProgress(100);
                    }

                    prod_txt.setText(units_produced + " out of \n" + daily_goal + " units");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        return myView;
    }
}