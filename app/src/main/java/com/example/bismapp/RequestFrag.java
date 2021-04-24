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
    //private RequestAdapter adapter;
    private NavigationActivity activity;
    public ArrayList<Request> requests;
    public RequestFrag requestList;
    private RequestAdapter adapter;

    // for the Intent
    private int changedIndex = -1;
    private Request changedRequest = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_member_roster, container, false);
        activity = (NavigationActivity) requireActivity();

        requests = new ArrayList<>();
        requests.add(new Request("12", "34", false));

        requestList = new RequestFrag();


        // set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        adapter = new RequestAdapter(requests, getActivity().getApplicationContext());

        RecyclerView request_list = view.findViewById(R.id.request_list);
        request_list.setLayoutManager(layoutManager);
        request_list.setAdapter(adapter);

        // setting up RecyclerView
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        teamRoster = (RecyclerView) view.findViewById(R.id.team_member_recycler);
        teamRoster.setHasFixedSize(true);
        teamRoster.setLayoutManager(layoutManager);
       adapter = new RequestAdapter(this, new ArrayList<>());
        teamRoster.setAdapter(adapter);*/

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