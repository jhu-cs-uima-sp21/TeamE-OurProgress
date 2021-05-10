package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AskForHelp extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "dbref at AskForHelp: ";

    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private HashMap<String, String> namesAndIDs;
    public ArrayAdapter<String> adapter;
    private ArrayList<String> names;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;
    private String supervisorID, teamName;
    private Context cntx;

    public AskForHelp() {
        // Required empty public constructor
    }

    public static AskForHelp newInstance(String param1, String param2) {
        AskForHelp fragment = new AskForHelp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        cntx = getActivity().getApplication();
        namesAndIDs = new HashMap<>();
        names = new ArrayList<>();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        peditor = myPrefs.edit();
        teamName = myPrefs.getString("TEAM", "N/A");

        View view;
        if (teamName.equals("N/A")) {
            view = inflater.inflate(R.layout.fragment_ask_for_help_no_team, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_ask_for_help, container, false);
        }

        getTeamMemberNames();
        System.out.println("names and ids:" + namesAndIDs.toString());
        System.out.println("names:" + names.toString());
        getSupervisor();
        System.out.print("Team: " + teamName + " Managed by: " + supervisorID);

        adapter = new ArrayAdapter<String>(cntx,
                R.layout.hint_item, names);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Search button
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String name = textView.getText().toString();
                Intent intent = new Intent(cntx, AskConfirmation.class);
                intent.putExtra("NAME", name);
                intent.putExtra("RECEIVERID", namesAndIDs.get(name));
                intent.putExtra("ISTEAM", false);
                startActivity(intent);
                textView.setText("");
            }
        });

        if (!teamName.equals("N/A")) {
            //team button
            Button teamBtn = (Button) view.findViewById(R.id.team);
            teamBtn.setOnClickListener(btnView -> {
                btnView.startAnimation(MainActivity.buttonClick);
                Intent intent = new Intent(cntx, AskConfirmation.class);
                intent.putExtra("NAME", teamName);
                intent.putExtra("RECEIVERID", teamName);
                intent.putExtra("ISTEAM", true);
                startActivity(intent);
            });

            //supervisor button
            Button supBtn = (Button) view.findViewById(R.id.supervisor);
            supBtn.setOnClickListener(btnView -> {
                if (myPrefs.getBoolean("MANAGER", false)) {
                    CharSequence text = "You cannot request help from yourself!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(cntx, text, duration);
                    toast.show();
                    return;

                }
                btnView.startAnimation(MainActivity.buttonClick);
                Intent intent = new Intent(cntx, AskConfirmation.class);
                intent.putExtra("NAME", "Supervisor");
                intent.putExtra("RECEIVERID", supervisorID);
                intent.putExtra("ISTEAM", false);
                startActivity(intent);

            });
        }


        return view;
    }

    private void getSupervisor() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> teamsShots = snapshot.child("teams").getChildren();
                for (DataSnapshot i : teamsShots) {
                    if (i.child("name").getValue(String.class).equals(teamName)){
                        supervisorID = i.child("managed_by").getValue(String.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //Reads users from firebase, then adds them to namesAndIDs, names!
    private void getTeamMemberNames() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                namesAndIDs.clear();
                names.clear();
                String prefUserID = myPrefs.getString("ID", "");
                Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                for (DataSnapshot i : usersShots) {

                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    if (userID.equals(prefUserID)) {
                        continue;
                    }
                    namesAndIDs.put(userName, userID);
                    names.add(userName);
                    System.out.println("name:" + userName + " ID:" + userID);
                }

                usersShots = snapshot.child("users").child("managers").getChildren();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    if (userID.equals(prefUserID)) {
                        continue;
                    }
                    namesAndIDs.put(userName, userID);
                    names.add(userName);
                    System.out.println("name:" + userName + " ID:" + userID);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}