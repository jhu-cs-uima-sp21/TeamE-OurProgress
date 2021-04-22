package com.example.bismapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskForHelp#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    //private AssociateNavigationActivity assocNav;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_for_help, container, false);
        namesAndIDs = new HashMap<>();
        names = new ArrayList<>();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        peditor = myPrefs.edit();

        getTeamMemberNames();
        System.out.println("names and ids:" + namesAndIDs.toString());
        System.out.println("names:" + names.toString());

        //TODO: Fix layout of the adapter for the AutoCompleteTextView
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.hint_item, names);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.search);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String name = textView.getText().toString();
                Intent intent = new Intent(getActivity().getApplicationContext(), AskConfirmation.class);
                intent.putExtra("NAME", name);
                intent.putExtra("RECIEVERID", namesAndIDs.get(name));
                intent.putExtra("ISTEAM", false);
                startActivity(intent);
                textView.setText("");
            }
        });
        return view;
    }


    //TODO: KEIDAI + CHIAMAKA LOOK HERE!
    //Reads users from firebase, then adds them to namesAndIDs, names!
    private void getTeamMemberNames() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String manager_id = myPrefs.getString("ID", "");

        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        // Fetch all teams managed by user
        // calling add value event listener method
        // for getting the values from database.
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values
                namesAndIDs.clear();
                names.clear();
                Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    namesAndIDs.put(userName, userID);
                    names.add(userName);
                    System.out.println("name:" + userName + " ID:" + userID);
                }

                usersShots = snapshot.child("users").child("managers").getChildren();
                for (DataSnapshot i : usersShots) {
                    String userName = i.child("Name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
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