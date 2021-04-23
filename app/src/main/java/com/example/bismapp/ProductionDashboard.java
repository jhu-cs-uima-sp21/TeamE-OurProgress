package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bismapp.ui.modifyTeams.ChangeMemberTeam;
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

        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        String teamID = myPrefs.getString("TEAM", "A");

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
                TextView per_text = (TextView) myView.findViewById(R.id.textView);
                TextView prod_txt = (TextView) myView.findViewById(R.id.prodText);
                per_text.setText(percent + "%");
                if (percent >=100){
                    per_text.setTextSize(42);
                    progressBar.setSecondaryProgress(100);
                }

                prod_txt.setText(units_produced + " out of \n" + daily_goal + " units");
                team_name_txt.setText("Team " + teamID + " has made");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        ImageButton settings_btn = (ImageButton) myView.findViewById(R.id.settingsButton);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                Intent intent = new Intent(getActivity(), EditTeam.class);
                intent.putExtra("Name", teamID);
                intent.putExtra("Goal", daily_goal);
                intent.putExtra("Members", getTeamMembers(teamID));
                startActivity(intent);
            }
        });

        return myView;
    }

    // Read from database to get list of team members
    private ArrayList<TeamMember> getTeamMembers(String teamName) {
        ArrayList<TeamMember> associates = new ArrayList<>();
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> teamMembers = snapshot.child("teams").child(teamName)
                        .child("team_members").getChildren();
                System.out.printf("Fetching all associates from team %s...\n", teamName);
                for (DataSnapshot i : teamMembers) {
                    String userName = i.child("name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    String station = i.child("station").getValue(String.class);
                    String team = i.child("team").getValue(String.class);
                    associates.add(new TeamMember(userName, userID, station, team));
                    System.out.printf("\tMember: %s,  ID: %s,  Station: %s, Team: %s\n", userName,
                            userID, station, team);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return associates;
    }
}