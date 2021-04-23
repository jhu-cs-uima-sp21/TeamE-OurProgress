package com.example.bismapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bismapp.ui.modifyTeams.ChangeMemberTeam;

import java.util.ArrayList;

public class RequestFrag extends Fragment {
    private RecyclerView teamRoster;
    private RequestAdapter adapter;
    private NavigationActivity activity;

    // for the Intent
    private int changedIndex = -1;
    private Request changedRequest = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_member_roster, container, false);
        activity = (NavigationActivity) requireActivity();

        // setting up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        teamRoster = (RecyclerView) view.findViewById(R.id.team_member_recycler);
        teamRoster.setHasFixedSize(true);
        teamRoster.setLayoutManager(layoutManager);
        adapter = new RequestAdapter(this, new ArrayList<>());
        teamRoster.setAdapter(adapter);

        return view;
    }

    public ArrayList<Request> getRequests() {
        return adapter.getRequests();
    }

    public RequestAdapter getRequestAdapter() {
        return adapter;
    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}