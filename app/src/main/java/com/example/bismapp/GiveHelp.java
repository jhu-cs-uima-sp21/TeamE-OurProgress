package com.example.bismapp;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

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
    private RequestAdapter adapter;
    private View view;
    private Context cntx;
    private static final String TAG = "dbref at YourTeams: ";


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
        cntx = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_give_help, container, false);

        //setContentView(R.layout.fragment_give_help);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        requests = new ArrayList<>();
        getRequests();
        //System.out.print("LOOK HERE FOR FIRST SENDER ID: " + requests.get(0).getSenderID());
        //requests.add(new Request("12", "34", false));

        // set up RecyclerView
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                //LinearLayoutManager.VERTICAL, false);
        /*adapter = new RequestAdapter(requests, getActivity().getApplicationContext());

        RecyclerView request_list = view.findViewById(R.id.request_list);

        request_list.setLayoutManager(layoutManager);
        request_list.setAdapter(adapter);*/

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

    private void getRequests() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("You are in on data change");
                requests.clear();
                Iterable<DataSnapshot> requestShots = snapshot.child("requests").getChildren();
                for (DataSnapshot i : requestShots) {
                    String receiverID = i.child("receiverID").getValue(String.class);
                    String senderID = i.child("senderID").getValue(String.class);
                    int numReq = i.child("numReq").getValue(Integer.class);
                    boolean team = i.child("team").getValue(Boolean.class);
                    //check if this users ID matches receiverID
                    if (receiverID.equals(myPrefs.getString("TEAM", "")) || receiverID.equals(myPrefs.getString("ID", ""))) {
                        requests.add(new Request(senderID, receiverID, team, numReq));
                        System.out.println(senderID);
                    }
                }
                for(Request r: requests) {
                    System.out.println(r.getReceiverID());
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(cntx,
                        LinearLayoutManager.VERTICAL, false);
                adapter = new RequestAdapter(requests, cntx);

                RecyclerView request_list = view.findViewById(R.id.request_list);

                request_list.setLayoutManager(layoutManager);
                request_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

}