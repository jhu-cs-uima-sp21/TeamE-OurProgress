package com.example.bismapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
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

        // set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        adapter = new TeamListAdapter(genLocalTeams());

        teamList  = findViewById(R.id.team_list);
        teamList.setHasFixedSize(true);
        teamList.setLayoutManager(layoutManager);
        teamList.setAdapter(adapter);

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
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Iterable<DataSnapshot> teamsSnapshot = snapshot.child("teams").getChildren();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(YourTeams.this, "Fail to get data.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return teams;
    }
}
