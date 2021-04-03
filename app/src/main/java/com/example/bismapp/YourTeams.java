package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.ui.modifyTeams.TeamFragment;
import com.example.bismapp.ui.modifyTeams.TeamInfoFragment;
import com.example.bismapp.ui.modifyTeams.TeamMRFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourTeams extends AppCompatActivity {

    FirebaseDatabase mdbase;
    DatabaseReference dbref;
    private static final String TAG = "dbref: ";
    RecyclerView teamList;
    TeamListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_teams);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

        ArrayList<Team> teams = new ArrayList<>();
        // Dummy list of teams
        for (int i = 0; i < 10; i++) {
            teams.add(new Team("Team #" + i, "", (i+94)*(i+7),
                    (i+736)*(54*i), null));
        }

        // set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        adapter = new TeamListAdapter(teams);

        teamList  = findViewById(R.id.team_list);
        teamList.setHasFixedSize(true);
        teamList.setLayoutManager(layoutManager);
        teamList.setAdapter(adapter);

        genLocalTeams();

        // tap each team and go to its respective dashboard
        // TODO
        RecyclerView team_list = findViewById(R.id.team_list);
        team_list.setOnClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // switch to Your Teams activity
                Team team;

                view.startAnimation(MainActivity.buttonClick);

                team_list.getAdapter().onBindViewHolder(team_list.findContainingViewHolder(view), position) {
                    team = (Team) team_list.get(position);
                    // TODO: there's no ReceyclerView equivalent for getItemAtPosision
                    // The only equivalents I've found are thru onBindViewHolder in the Adapter
                    // But we don't have an adapter. Tried doing it this way. Not working as of now.
                    // See links Keidai sent in Slack for more info
                };

                String name = team.getName();
                String managed_by = team.getManaged_by();
                int units_produced = team.getUnits_produced();
                int dailyGoal = team.getDaily_goal();

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("manager by", managed_by);
                bundle.putInt("units produced", units_produced);
                bundle.putInt("daily goal", dailyGoal);

                Intent intent = new Intent(getApplicationContext(), ProductionDashboard.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // set up Team Floating Button
        ImageButton create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(view -> { // switch to Your Teams activity
            view.startAnimation(MainActivity.buttonClick);
            Intent intent = new Intent(getApplicationContext(), CreateTeam.class);
            startActivity(intent);
        });
    }

    private ArrayList<Team> genLocalTeams() {
        ArrayList<Team> teams = new ArrayList<>();

        // Fetch all teams managed by user
        // calling add value event listener method
        // for getting the values from database.
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                Iterable<DataSnapshot> teamsShots = snapshot.child("teams").getChildren();
                for (DataSnapshot i : teamsShots) {
                    System.out.println(i.getKey());
                }

                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return teams;
    }
}
