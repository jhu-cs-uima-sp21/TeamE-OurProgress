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
import android.widget.CheckBox;
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
import java.util.HashSet;

public class YourTeams extends AppCompatActivity {

    private DatabaseReference dbref;
    private static final String TAG = "dbref at YourTeams: ";

    private TeamListAdapter adapter;
    private ArrayList<Team> teams;
    public static HashSet<String> teamNames = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_teams);
        FirebaseDatabase mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        teams = new ArrayList<>();
        genLocalTeams();

        // set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        adapter = new TeamListAdapter(teams, getApplicationContext());

        RecyclerView team_list = findViewById(R.id.team_list);
        team_list.setLayoutManager(layoutManager);
        team_list.setAdapter(adapter);

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

    private void genLocalTeams() {
        //ArrayList<Team> tmp_teams = new ArrayList<>();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String manager_id = myPrefs.getString("ID", "");

        // Fetch all teams managed by user
        // calling add value event listener method
        // for getting the values from database.
        dbref.addValueEventListener(new ValueEventListener() {
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
                    //System.out.println(i.getKey());
                    //System.out.println(i.getValue(Team.class));
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
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        genLocalTeams();
        //adapter.updateDataSet(teams);
        adapter.notifyDataSetChanged();
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        adapter = new TeamListAdapter(teams, getApplicationContext());

        team_list = findViewById(R.id.team_list);
        team_list.setHasFixedSize(true);
        team_list.setLayoutManager(layoutManager);
        team_list.setAdapter(adapter);*/
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            //adapter.updateDataSet(teams);
            adapter.notifyDataSetChanged();
        }

    }

}
