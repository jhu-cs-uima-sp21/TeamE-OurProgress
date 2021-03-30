package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bismapp.MainActivity;
import com.example.bismapp.R;

public class TeamMRFragment extends Fragment {
    private Fragment teamMember;
    private RecyclerView teamRoster;
    private MainActivity myact;
    Context cntx;

    @Override
    public View onCreate(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_team_member_roster, container, false);

        cntx = getActivity().getApplicationContext();

        myact = (MainActivity) getActivity();

        teamRoster = (RecyclerView) myview.findViewById(R.id.team_member_recycler);
        //ToDo

    }
}
