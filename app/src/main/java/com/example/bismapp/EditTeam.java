

package com.example.bismapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class EditTeam extends AppCompatActivity {
    public TeamInfoFragment teamInfo;
    public TeamMRFragment teamRoster;
    private OkCancelFragment okCancel;
    public ArrayList<TeamMember> associates;
    public ArrayList<String> associatesNames;
    public HashMap<String, String> associatesToTeamChange;

    private String preName;

    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;
    private static final String TAG = "dbref at CreateTeams: ";
    private static final String TEAM_TAG = "Invalid team: ";

    private String managerID;
    private Integer unitsProduced, dailyGoal;
    private ArrayList<TeamMember> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        peditor = myPrefs.edit();
        associates = new ArrayList<>();
        associatesNames = new ArrayList<>();
        associatesToTeamChange = new HashMap<>();

        teamRoster = new TeamMRFragment();
        teamInfo = new TeamInfoFragment();
        okCancel = new OkCancelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.okay_cancel_frag, okCancel).commit();

        // pre-populate
        preName = myPrefs.getString("TEAM", "A");

        disableEditingTeamName();
    }

    private void disableEditingTeamName() {
        EditText editText = ((EditText) findViewById(R.id.edit_team_name));
        editText.setText("Edit Team " + preName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        } else {
            editText.setTextSize(38);
            editText.setMaxLines(2);
        }
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(ContextCompat.getColor(this, R.color.bism_blue));
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));
        editText.setContentDescription("Name of the team being edited: " + preName);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.bism_blue));
        linearLayout.setPadding(0,0,0,0);

        findViewById(R.id.imageView).setVisibility(View.GONE);
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
        managerID = myPrefs.getString("ID", "N/A");
        members = teamRoster.getTeamMembers();

        try {
            dailyGoal = Integer.parseInt(((EditText) findViewById(R.id.enterDailyGoal)).getText()
                    .toString());
            if (dailyGoal == 0) {
                ((EditText) findViewById(R.id.enterDailyGoal)).setText("");
                makeToast("Daily goal cannot be 0 nor greater than " + Integer.MAX_VALUE);
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
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                // get team's progress
                System.out.println("This is the team name:" + preName);
                unitsProduced = snapshot.child("teams").child(preName).child("units_produced")
                        .getValue(Integer.class);
                // update team values of members in firebase
                for (TeamMember associate : members) {
                    associate.setTeam(preName);
                    dbref.child("users").child("associates").child(associate.getID()).child("team")
                            .setValue(preName);
                    Log.d(TAG, "Set " + associate.getName() + "'s team to " + preName);
                }

                peditor.putString("TEAM", preName);
                peditor.apply();

                System.out.println("This is the team name:" + preName);
                System.out.println("This is the manager ID:" + managerID);
                System.out.println("This is the units produced:" + unitsProduced);
                System.out.println("This is the daily goal:" + dailyGoal);

                // replace with new edited
                Team team = new Team(preName, managerID, unitsProduced, dailyGoal, members);
                dbref.child("teams").child(preName).setValue(team);
                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        changeOldTeams();
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
                for (String memberID : associatesToTeamChange.keySet()) {
                    String teamName = associatesToTeamChange.get(memberID);
                    ArrayList<HashMap<String, String>> teamMembers = (ArrayList<HashMap<String, String>>)
                            snapshot.child("teams").child(teamName).child("team_members").getValue();
                    for (HashMap<String, String> member : teamMembers) {
                        if (member.get("id").equals(memberID)) {
                            teamMembers.remove(member);
                            break;
                        }
                    }
                    dbref.child("teams")
                                .child(teamName).child("team_members").setValue(teamMembers);
                    if (snapshot.child("users").child(memberID).child("team")
                            .getValue(String.class).equals(preName)) {
                        dbref.child("users").child(memberID).child("team").setValue("N/A");
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