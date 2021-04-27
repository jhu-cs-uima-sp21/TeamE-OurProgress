package com.example.bismapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.ui.modifyTeams.ChangeMemberTeam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class YourTeams extends AppCompatActivity {

    private DatabaseReference dbref;
    private static final String TAG = "dbref at YourTeams: ";
    private final int LAUNCH_CHANGE_TEAM = 4;

    private TeamListAdapter adapter;
    private ArrayList<Team> teams;
    public static HashSet<String> teamNames = new HashSet<>();

    private FirebaseDatabase mdbase;
    private ValueEventListener valueEventListener;

    private Boolean clicked = false;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private FloatingActionButton edit_your_teams_btn;
    private FloatingActionButton add_team_btn;
    private FloatingActionButton del_team_btn;
    private TextView title;
    private String name;

    public boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_teams);
        FirebaseDatabase mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        teams = new ArrayList<>();
        title = findViewById(R.id.your_teams);

        rotateOpen = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getApplication(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getApplication(), R.anim.to_bottom_anim);
        edit_your_teams_btn = findViewById(R.id.edit_your_teams_btn);
        add_team_btn = findViewById(R.id.add_team_btn);
        del_team_btn = findViewById(R.id.del_team_btn);

        genLocalTeams();

        // set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        adapter = new TeamListAdapter(teams, getApplicationContext(), this);

        RecyclerView team_list = findViewById(R.id.team_list);
        team_list.setLayoutManager(layoutManager);
        team_list.setAdapter(adapter);

        // set up individual team clickListener
        adapter.setOnItemClickListener((position, v) ->
                Log.d("Team", "onItemClick position: " + position));

        // set up Team Floating Button
        edit_your_teams_btn = findViewById(R.id.edit_your_teams_btn);
        edit_your_teams_btn.setOnClickListener(view -> { // switch to Your Teams activity
            onEYTBtnClicked();
        });
        
        add_team_btn = findViewById(R.id.add_team_btn);
        add_team_btn.setOnClickListener(view -> { // switch to Your Teams activity
            view.startAnimation(MainActivity.buttonClick);
            Intent intent = new Intent(getApplicationContext(), CreateTeam.class);
            startActivity(intent);
        });

        del_team_btn = findViewById(R.id.del_team_btn);
        del_team_btn.setOnClickListener(view -> { // switch to Your Teams activity
            deleteMode = true;
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "You are now entering in team delete mode", Toast.LENGTH_SHORT).show();
            title.setText("Delete Teams");
        });
    }

    // floating action button methods
    private void onEYTBtnClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    // floating action button methods
    private void setVisibility(Boolean clicked) {
        if(!clicked) {
            add_team_btn.setVisibility(View.VISIBLE);
            del_team_btn.setVisibility(View.VISIBLE);
        } else {
            add_team_btn.setVisibility(View.INVISIBLE);
            del_team_btn.setVisibility(View.INVISIBLE);
        }
    }

    // floating action button methods
    private void setAnimation(Boolean clicked) {
        if(!clicked) {
            add_team_btn.startAnimation(fromBottom);
            del_team_btn.startAnimation(fromBottom);
            edit_your_teams_btn.startAnimation(rotateOpen);
        } else {
            add_team_btn.startAnimation(toBottom);
            del_team_btn.startAnimation(toBottom);
            edit_your_teams_btn.startAnimation(rotateClose);
        }
    }

    public void deleteTeam(String name) {
        Intent intent = new Intent(this, ChangeMemberTeam.class);
        intent.putExtra("Method", "team");
        intent.putExtra("Team", name);
        startActivityForResult(intent, LAUNCH_CHANGE_TEAM);
        this.name = name;
    }

    private void genLocalTeams() {
        //ArrayList<Team> tmp_teams = new ArrayList<>();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String manager_id = myPrefs.getString("ID", "");

        // Fetch all teams managed by user
        // calling add value event listener method
        // for getting the values from database.
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                ArrayList<Team> tmp_teams = new ArrayList<>();
                Iterable<DataSnapshot> teamsShots = snapshot.child("teams").getChildren();
                for (DataSnapshot i : teamsShots) {
                    String managed_by = i.child("managed_by").getValue(String.class);
                    if(managed_by.equals(manager_id)) {
                        String name = i.child("name").getValue(String.class);
                        Integer units_produced = i.child("units_produced").getValue(Integer.class);
                        Integer daily_goal = i.child("daily_goal").getValue(Integer.class);
                        System.out.println(name);
                        tmp_teams.add(new Team(name, managed_by, units_produced, daily_goal, null));
                        teamNames.add(name);
                    }
                }

                adapter.updateDataSet(tmp_teams);
                adapter.notifyDataSetChanged();

                teams.clear();
                teams.addAll(tmp_teams);

                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        dbref.addValueEventListener(valueEventListener);

    }

    @Override
    public void onBackPressed() {
        if (deleteMode) {
            deleteMode = !deleteMode;
            adapter.notifyDataSetChanged();
            title.setText("Your Teams");
            Toast.makeText(this, "You are now no longer in team deletion mode", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        genLocalTeams();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CHANGE_TEAM) {
            if (resultCode == Activity.RESULT_OK) {
                mdbase = FirebaseDatabase.getInstance();
                dbref = mdbase.getReference();
                // Fetch all associates
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<HashMap<String, String>> teamMembers = (ArrayList<HashMap<String, String>>) snapshot.child("teams").child(name).child("team_members").getValue();
                        System.out.println("Removing all members from team ...");
                        for (HashMap<String, String> i : teamMembers) {
                            dbref.child("users").child("associates").child(i.get("id")).child("team").setValue("N/A");
                        }

                        dbref.child("teams").child(name).removeValue();
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Team " + name + " was not deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        dbref.removeEventListener(valueEventListener);
        super.onPause();
    }
}
