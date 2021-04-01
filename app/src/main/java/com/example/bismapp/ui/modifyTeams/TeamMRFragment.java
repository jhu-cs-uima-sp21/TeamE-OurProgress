package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bismapp.MainActivity;
import com.example.bismapp.R;
import com.example.bismapp.TeamMemberAdapter;

public class TeamMRFragment extends Fragment {

    private RecyclerView teamRoster;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_member_roster, container, false);

        // setting up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        TeamMemberAdapter adapter = new TeamMemberAdapter(getActivity(), MainActivity.teamMembers);

        teamRoster = (RecyclerView) view.findViewById(R.id.team_member_recycler);
        teamRoster.setHasFixedSize(true);
        teamRoster.setLayoutManager(layoutManager);
        teamRoster.setAdapter(adapter);
        //registerForContextMenu(teamRoster);

        return view;
    }
}