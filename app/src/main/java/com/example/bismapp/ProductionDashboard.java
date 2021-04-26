package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.bismapp.ui.modifyTeams.ChangeMemberTeam;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductionDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductionDashboard extends Fragment {
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private ValueEventListener valueEventListener;
    private DatabaseReference newTeamRef;
    private ChildEventListener teamEventListener;
    private static final String TAG = "dbref: ";
    private int daily_goal, units_produced, percent;
    private boolean hasPaused;

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

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        peditor = myPrefs.edit();
        newTeamRef =  dbref.child("teams")
                .child(myPrefs.getString("TEAM", "A"));
        hasPaused = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_production_dashboard, container, false);
        View myView = inflater.inflate(R.layout.fragment_production_dashboard, container, false);
        Context cntx = getActivity().getApplicationContext();

        buttonClick.setDuration(100);

        //READ FROM DATABASE TO CHECK IF MANAGER
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                String teamID = myPrefs.getString("TEAM", "A");
                System.out.println(snapshot.child("teams").child(teamID).exists());
                System.out.println("ProductionDash: THis is the teamID " + teamID);
                DataSnapshot first = snapshot.child("teams").child(teamID);
                System.out.println("Production DashBoard first" + first.toString());
                System.out.println("Production DashBoard DataSnap" + first.child("daily_goal").toString());
                daily_goal = snapshot.child("teams").child(teamID).child("daily_goal").getValue(Integer.class);
                peditor.putInt("DAILY_GOAL", daily_goal);
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
                } else  {
                    draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_green);
                }

                TextView team_name_txt = (TextView) myView.findViewById(R.id.hasMade);
                progressBar.setProgressDrawable(draw);
                progressBar.setSecondaryProgress(percent);
                progressBar.setProgress(percent);
                TextView per_text = (TextView) myView.findViewById(R.id.textView);
                TextView prod_txt = (TextView) myView.findViewById(R.id.prodText);
                per_text.setText(percent + "%");
                if (percent >=100){
                    per_text.setTextSize(42);
                    progressBar.setSecondaryProgress(100);
                    progressBar.setProgress(100);
                }

                prod_txt.setText(units_produced + " out of \n" + daily_goal + " units");
                team_name_txt.setText("Team " + teamID + " has made");
                peditor.apply();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        dbref.addValueEventListener(valueEventListener);

        ImageButton settings_btn = (ImageButton) myView.findViewById(R.id.settingsButton);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                Intent intent = new Intent(getActivity(), EditTeam.class);
                startActivity(intent);
            }
        });

        return myView;
    }

    @Override
    public void onPause() {
        super.onPause();
        // dbref.removeEventListener(valueEventListener);
        /*if (teamEventListener != null) {
            newTeamRef.removeEventListener(teamEventListener);
        }
        hasPaused = true;*/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (hasPaused) {
            // wait till child been fully added
            newTeamRef = dbref.child("teams")
                    .child(myPrefs.getString("TEAM", "A"));
            teamEventListener = new ChildEventListener() {
                String nameCheck = myPrefs.getString("TEAM", "A");

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.hasChild("name") && snapshot.hasChild("daily_goal")
                            && snapshot.hasChild("units_produced")) {
                        dbref.addValueEventListener(valueEventListener);
                        newTeamRef.removeEventListener(teamEventListener);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Nothing
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    // Nothing
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Nothing
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "onResume-Failed to read value.", error.toException());
                }
            };
            newTeamRef.addChildEventListener(teamEventListener);
        }*/
    }
}