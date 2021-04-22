package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;

import java.util.ArrayList;

public class TeamMRFragment extends Fragment {

    private RecyclerView teamRoster;
    private TeamMemberAdapter adapter;
    private CreateTeam activity;
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

    public void removeTeamMember(int index, String name) throws Exception {
        TeamMember member = adapter.teamMembers.get(index);
        member.setTeam("N/A");
        // Update AutoCompleteTextView adapter
        activity.getAssociate(name).setTeam("N/A");
        activity.teamInfo.adapter.add(name);
        activity.teamInfo.adapter.notifyDataSetChanged();
        // Update Roster adapter
        adapter.teamMembers.remove(index);
        adapter.notifyDataSetChanged();
    }
}

