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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class YourTeams extends AppCompatActivity {

    FirebaseDatabase mdbase;
    DatabaseReference dbref;
    private static final String TAG = "dbref at YourTeams: ";
    RecyclerView team_list;
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

        team_list = findViewById(R.id.team_list);
        team_list.setHasFixedSize(true);
        team_list.setLayoutManager(layoutManager);
        team_list.setAdapter(adapter);

        genLocalTeams();

        // set up individual team clickListener
        adapter.setOnItemClickListener((position, v) ->
                Log.d("Team", "onItemClick position: " + position));

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
            public void onDataChange(@NotNull DataSnapshot snapshot) {
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
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return teams;
    }
}
