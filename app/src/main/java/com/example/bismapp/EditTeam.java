
package com.example.bismapp;

import android.app.Activity;
import android.content.Intent;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class EditTeam extends AppCompatActivity {
    public TeamInfoFragment teamInfo;
    public TeamMRFragment teamRoster;
    private OkCancelFragment okCancel;
    public ArrayList<TeamMember> associates;
    public ArrayList<String> associatesNames;
    public HashMap<String, String> associatesToTeamChange;

    public Bundle bundle;
    private String preName;

    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private static final String TAG = "dbref at CreateTeams: ";
    private static final String TEAM_TAG = "Invalid team: ";

    private String teamName, managerID;
    private Integer unitsProduced, dailyGoal;
    private ArrayList<TeamMember> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        associates = new ArrayList<>();
        associatesNames = new ArrayList<>();
        associatesToTeamChange = new HashMap<>();

        // getting the bundle back from the android
        bundle = getIntent().getExtras();

        teamRoster = new TeamMRFragment();
        teamInfo = new TeamInfoFragment();
        okCancel = new OkCancelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.okay_cancel_frag, okCancel).commit();

        // pre-populate
        preName = bundle.getString("Name");
        EditText editName = ((EditText) findViewById(R.id.edit_team_name));
        editName.setText(preName);

    }

    public void makeToast(CharSequence text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void cancelButtonClicked() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
    }

    public void okButtonClicked() {
        // form new team with user input
        teamName = ((EditText) findViewById(R.id.edit_team_name)).getText().toString();
        managerID = myPrefs.getString("ID", "N/A");
        members = teamRoster.getTeamMembers();

        // if any information is missing, do not make team
        if (teamName.equals("")) {
            makeToast("Please enter a team name");
            Log.d(TEAM_TAG, "team name");
            return;
        } else if (teamName.equals("N/A") || teamName.equals("TBD")) {
            makeToast("Please enter a team name that is not \"N/A\" or \"TBD\"");
            Log.d(TEAM_TAG, "team name");
            return;
        } else if (!preName.equals(teamName) && YourTeams.teamNames.contains(teamName)) {
            makeToast("Team \"" + teamName + "\" has already been created");
            makeToast(teamName + " is not the same as " + preName);
            return;
        }
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
        // Update values of team
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            // TODO: Update list of teams
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                // get team's progress
                unitsProduced = snapshot.child("teams").child(teamName).child("units_produced")
                        .getValue(Integer.class);
                // update team values of members in firebase
                for (TeamMember associate : members) {
                    associate.setTeam(teamName);
                    dbref.child("users").child("associates").child(associate.getID()).child("team")
                            .setValue(teamName);
                    Log.d(TAG, "Set " + associate.getName() + "'s team to " + teamName);
                }
                // update values of the team's branch on firebase
                dbref.child("teams").child(preName).removeValue();
                // replace with new edited
                Team team = new Team(teamName, managerID, unitsProduced, dailyGoal, members);
                dbref.child("teams").child(team.getName()).setValue(team);
                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        changeOldTeams();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Name", teamName);
        returnIntent.putExtra("Goal", dailyGoal);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void getAllAssociates() {
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        // Fetch all associates
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                System.out.println("Fetching all associates ...");
                associates.clear();
                associatesNames.clear();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    String station = i.child("station").getValue(String.class);
                    String team = i.child("team").getValue(String.class);
                    associates.add(new TeamMember(userName, userID, station, team));
                    associatesNames.add(userName);
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
    }

    public TeamMember getAssociate(String name) throws Exception {
        for (TeamMember curr : associates) {
            if (curr.getName().equals(name)) {
                return curr;
            }
        }
        throw new Exception("No associate with name " + name + " found");
    }

    public void removeAssociateFromOldTeam(TeamMember member) {
        associatesToTeamChange.put(member.getID(), member.getTeam());
    }

    public void notRemoveAssociateFromOldTeam(String id) {
        associatesToTeamChange.remove(id);
    }

    private void changeOldTeams() {
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        // Fetch all associates
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Removing team members from their previous teams...");
                for (String i : associatesToTeamChange.keySet()) {
                    String teamName = associatesToTeamChange.get(i);
                    ArrayList<HashMap<String, String>> teamMembers = (ArrayList<HashMap<String, String>>)
                            snapshot.child("teams").child(teamName).child("team_members").getValue();
                    for (HashMap<String, String> member : teamMembers) {
                        if (member.get("id").equals(i)) {
                            teamMembers.remove(member);
                            break;
                        }
                    }
                    if (teamMembers.isEmpty()) {
                        dbref.child("teams").child(teamName).removeValue();
                    } else {
                        dbref.child("teams")
                                .child(teamName).child("team_members").setValue(teamMembers);
                    }
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}