
package com.example.bismapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bismapp.ui.modifyTeams.TeamInfoFragment;
import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

public class CreateTeam extends AppCompatActivity {
    private TeamInfoFragment teamInfo;
    private TeamMRFragment teamRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        teamInfo = new TeamInfoFragment();
        teamRoster = new TeamMRFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.team_roster_frag, teamRoster).commit();
    }
}