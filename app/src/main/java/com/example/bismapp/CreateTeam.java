
package com.example.bismapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bismapp.ui.modifyTeams.TeamInfoFragment;
import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

public class CreateTeam extends AppCompatActivity {
    private TeamInfoFragment teamInfo;
    private TeamMRFragment teamRoster;
    private OkCancelFragment okCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        teamInfo = new TeamInfoFragment();
        teamRoster = new TeamMRFragment();
        okCancel = new OkCancelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.okay_cancel_frag, okCancel).commit();
    }
}