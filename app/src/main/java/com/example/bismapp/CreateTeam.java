
package com.example.bismapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bismapp.ui.modifyTeams.TeamInfoFragment;
import com.example.bismapp.ui.modifyTeams.TeamMRFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateTeam extends AppCompatActivity {
    private TeamInfoFragment teamInfo;
    private TeamMRFragment teamRoster;
    private OkCancelFragment okCancel;

    FirebaseDatabase mdbase;
    DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private static final String TAG = "dbref at CreateTeams: ";
    private static final String TEAM_TAG = "Invalid team: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        teamInfo = new TeamInfoFragment();
        teamRoster = new TeamMRFragment();
        okCancel = new OkCancelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.okay_cancel_frag, okCancel).commit();
    }

    public void cancelButtonClicked() {
        finish();
    }

    public void makeToast(CharSequence text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }
    public void okButtonClicked() {
        // form new team with user input
        String teamName = ((EditText)findViewById(R.id.create_teams)).getText().toString();
        String managerID = myPrefs.getString("ID", "N/A");
        Integer dailyGoal;
        try {
            dailyGoal = Integer.parseInt(((EditText) findViewById(R.id.enterDailyGoal)).getText()
                    .toString());
        } catch (NumberFormatException e) {
            makeToast("Please enter a daily goal");
            return;
        }
        ArrayList<TeamMember> members = teamRoster.getTeamMembers();

        // if any information is missing, do not make team
        boolean invalidTeam = false;
        if (teamName.equals("")) {
            makeToast("Please enter a team name");
            Log.d(TEAM_TAG, "team name");
            return;
        }
        if (members == null || members.size() < 1) {
            makeToast("Please add members to the team");
            Log.d(TEAM_TAG, "team members");
            return;
        }

        Team team = new Team(teamName, managerID, 0, dailyGoal, members);

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                dbref.child("teams").child(team.getName()).setValue(team);
                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        finish();
    }
}