
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
import java.util.HashMap;
import java.util.HashSet;

public class CreateTeam extends AppCompatActivity {
    public TeamInfoFragment teamInfo;
    public TeamMRFragment teamRoster;
    private OkCancelFragment okCancel;
    public HashSet<String> membersOnList;
    public ArrayList<TeamMember> associates;
    public ArrayList<String> associatesNames;

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
        membersOnList = new HashSet<>();
        associates = new ArrayList<>();
        associatesNames = new ArrayList<>();

        teamRoster = new TeamMRFragment();
        teamInfo = new TeamInfoFragment();
        okCancel = new OkCancelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.team_info_frag, teamInfo)
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
        ArrayList<TeamMember> members = teamRoster.getTeamMembers();

        // if any information is missing, do not make team
        if (teamName.equals("")) {
            makeToast("Please enter a team name");
            Log.d(TEAM_TAG, "team name");
            return;
        } else if (YourTeams.teamNames.contains(teamName)) {
            makeToast("Team \"" + teamName + "\" has already been created");
            return;
        }
        Integer dailyGoal;
        try {
            dailyGoal = Integer.parseInt(((EditText) findViewById(R.id.enterDailyGoal)).getText()
                    .toString());
            if (dailyGoal == 0) {
                ((EditText) findViewById(R.id.enterDailyGoal)).setText("");
                makeToast("Daily goal cannot be 0");
                return;
            }
        } catch (NumberFormatException e) {
            makeToast("Please enter a daily goal");
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

    public void updateInfoAdapter() {
        teamInfo.updateMemberSearch();
    }

    //public void removeMemberFromInfo()

    /*public ArrayList<String> getTeamMemberNames() {
        MainActivity mainActivity = (MainActivity)(getParent().getParent());
        mainActivity.getAllAssociates();
        return mainActivity.associatesNames;
    }

    public TeamMember getAssociate(String name) throws Exception {
        MainActivity mainActivity = (MainActivity)(getParent().getParent());
        return mainActivity.getAssociate(name);
    }*/
    public void getAllAssociates() {
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        associates.clear();
        associatesNames.clear();

        // Fetch all associates
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                System.out.println("Fetching all associates ...");
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    associates.add(new TeamMember(userName, userID));
                    associatesNames.add(userName);

                    System.out.printf("\tMember: %s ID: %s\n", userName, userID);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public TeamMember getAssociate(String name) throws Exception {
        TeamMember theMember = null;
        TeamMember currMember;

        for (int i = 0; i < associates.size(); i++) {
            currMember = associates.get(i);
            if (currMember.getName().equals(name)) {
                theMember = currMember;
                break;
            }
        }
        if (theMember == null) {
            throw new Exception("Null team member");
        }
        return theMember;
    }
}