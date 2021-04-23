package com.example.bismapp.ui.modifyTeams;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;

import java.util.ArrayList;

public class TeamMRFragment extends Fragment {

    private RecyclerView teamRoster;
    private TeamMemberAdapter adapter;
    private CreateTeam activity;
    private final int LAUNCH_REMOVE_MEMBER = 2;

    // for the Intent
    private int changedIndex = -1;
    private TeamMember changedMember = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_member_roster, container, false);
        activity = (CreateTeam)requireActivity();

        // setting up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        teamRoster = (RecyclerView) view.findViewById(R.id.team_member_recycler);
        teamRoster.setHasFixedSize(true);
        teamRoster.setLayoutManager(layoutManager);
        adapter = new TeamMemberAdapter(this, new ArrayList<TeamMember>());
        teamRoster.setAdapter(adapter);

        return view;
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return adapter.getTeamMembers();
    }

    public TeamMemberAdapter getTeamMemberAdapter() {
        return adapter;
    }

    public void checkToRemoveTeamMember(int index, TeamMember member) {
        Intent intent = new Intent(getActivity(), ChangeMemberTeam.class);
        intent.putExtra("Method", "remove");
        intent.putExtra("Name", member.getName());
        intent.putExtra("Station", member.getStation());
        changedIndex = index;
        changedMember = member;
        startActivityForResult(intent, LAUNCH_REMOVE_MEMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (changedMember == null) { return; }

        if (requestCode == LAUNCH_REMOVE_MEMBER) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    removeTeamMember(changedIndex, changedMember.getName());
                } catch (Exception e) {
                    makeToast("Error occurred: Was not able to remove associate from roster");
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                makeToast(changedMember.getName() + " was not removed from the team");
            }
        }
    }

    public void removeTeamMember(int index, String name) throws Exception {
        TeamMember member = adapter.teamMembers.get(index);
        member.setTeam("N/A");
        // Update AutoCompleteTextView adapter
        activity.getAssociate(name).setTeam("N/A"); // throws exception
        activity.teamInfo.adapter.add(name);
        activity.teamInfo.adapter.notifyDataSetChanged();
        // Update Roster adapter
        adapter.teamMembers.remove(index);
        adapter.notifyDataSetChanged();
    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}

