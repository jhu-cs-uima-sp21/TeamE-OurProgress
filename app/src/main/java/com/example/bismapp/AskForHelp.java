package com.example.bismapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskForHelp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskForHelp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "dbref at AskForHelp: ";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase mdbase;
    DatabaseReference dbref;
    private ArrayList<TeamMember> namesAndIDs;
    public ArrayAdapter<String> adapter;
    private ArrayList<String> names;

    public AskForHelp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskForHelp.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_for_help, container, false);
        namesAndIDs = new ArrayList<>();
        names = new ArrayList<>();
        getTeamMemberNames();
        //namesAndIDs = activity.getTeamMemberNames();
        //addAllIfNotNull(namesAndIDs, activity.getTeamMembersIDs());
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, names);

        // TODO: If the search function fails, uncomment this!
        //updateMemberSearch();

        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.enterNames);

        textView.setAdapter(adapter);

        return view;
    }

    private void getTeamMemberNames() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String manager_id = myPrefs.getString("ID", "");

        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        System.out.println("Hellow");
        // Fetch all teams managed by user
        // calling add value event listener method
        // for getting the values from database.
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values

                ArrayList<TeamMember> users = new ArrayList<>();
                ArrayList<String> userNames = new ArrayList<>();
                Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);

                    users.add(new TeamMember(userName, userID));
                    userNames.add(userName);

                    System.out.println(userName);
                    System.out.println(userID);
                }

                usersShots = snapshot.child("users").child("managers").getChildren();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);

                    users.add(new TeamMember(userName, userID));
                    userNames.add(userName);

                    System.out.println(userName);
                    System.out.println(userID);
                }

                namesAndIDs.clear();
                namesAndIDs.addAll(users);

                names.clear();
                names.addAll(userNames);

                /*ArrayList<Team> tmp_teams = new ArrayList<>();
                Iterable<DataSnapshot> teamsShots = snapshot.child("teams").getChildren();
                for (DataSnapshot i : teamsShots) {
                    String managed_by = i.child("managed_by").getValue(String.class);
                    if(managed_by.equals(manager_id)) {
                        String name = i.child("name").getValue(String.class);
                        Integer units_produced = i.child("units_produced").getValue(Integer.class);
                        Integer daily_goal = i.child("daily_goal").getValue(Integer.class);
                        System.out.println(name);
                        tmp_teams.add(new Team(name, managed_by, units_produced, daily_goal, null));
                    }
                    //System.out.println(i.getKey());
                    //System.out.println(i.getValue(Team.class));
                }

                adapter.updateDataSet(tmp_teams);
                adapter.notifyDataSetChanged();

                teams.clear();

                teams.addAll(tmp_teams);

                Log.d(TAG, "Children count: " + snapshot.getChildrenCount());*/
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}