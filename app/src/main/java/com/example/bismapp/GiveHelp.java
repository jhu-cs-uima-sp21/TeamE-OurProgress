package com.example.bismapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bismapp.ui.modifyTeams.TeamInfoFragment;
import com.example.bismapp.ui.modifyTeams.TeamMRFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiveHelp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiveHelp extends Fragment {
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    public ArrayList<Request> requests;
    public RequestFrag requestList;
    private RequestAdapter adapter;


    public GiveHelp() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GiveHelp newInstance(String param1, String param2) {
        GiveHelp fragment = new GiveHelp();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_give_help, container, false);

        //setContentView(R.layout.fragment_give_help);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        //myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

        // set up individual team clickListener
       /* adapter.setOnItemClickListener((position, v) ->
                Log.d("Team", "onItemClick position: " + position));*/

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_roster_frag, teamRoster)
                .replace(R.id.team_info_frag, teamInfo)
                .replace(R.id.okay_cancel_frag, okCancel).commit();
        */
        return view;

    }
}