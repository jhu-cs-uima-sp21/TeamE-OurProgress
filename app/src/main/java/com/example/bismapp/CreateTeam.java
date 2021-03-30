package com.example.bismapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

public class CreateTeam extends AppCompatActivity {
    private TeamMRFragment teamRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        teamRoster = new TeamMRFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, teamRoster).commit();
    }
}
