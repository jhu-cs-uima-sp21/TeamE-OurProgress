package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.MainActivity;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamInfoFragment extends Fragment {

    /* TODO: remove?
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;*/
    private static final String TAG = "dbref at YourTeams: ";
    public ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        /* TODO: possibly rmeove
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();*/

        CreateTeam activity = (CreateTeam) requireActivity();

        activity.getAllAssociates();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, activity.associatesNames);
        updateMemberSearch();

        /*String[] tempTeamMembers = ((CreateTeam) requireActivity()).teamRoster
                .getTeamMemberAdapter().getTeamMemberNames();
        */
        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.enterNames);
        textView.setAdapter(adapter);
        // does touch not work with emulator, or is my code not working?
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeKeyboard(textView);
                String newTeamMemberName = textView.getText().toString();
                TeamMember newTeamMember;
                String newTeamMemberTeam[] = {null};
                try {
                    newTeamMemberTeam[0] = ((CreateTeam) requireActivity()).getAssociateTeam(newTeamMemberName);

                    if (!newTeamMemberTeam[0].equals("N/A")) {
                        Toast toast = Toast.makeText(((CreateTeam) requireActivity()),
                                newTeamMemberName
                                        + " is already on Team "
                                        + newTeamMemberTeam[0], Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println("The toast said that the team was: " + newTeamMemberTeam);
                        textView.setText("");
                        return;
                    }

                    TeamMember newMember;
                    try {
                        newMember = activity.getAssociate(newTeamMemberName);
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(((CreateTeam) requireActivity()),
                                "Can not find member, " + newTeamMemberName, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    activity.teamRoster.getTeamMemberAdapter().addTeamMembers(newMember);
                    newMember.setOnTeam(true);
                    activity.associatesNames.remove(newTeamMemberName);
                    Toast toast = Toast.makeText(((CreateTeam) requireActivity()),
                            newTeamMemberName
                                    + " has been added to the team", Toast.LENGTH_SHORT);
                    toast.show();
                    textView.setText("");
                } catch (Exception e) {
                    System.out.println("NULL TEAM MEMBER");
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMemberSearch();
    }

    public void updateMemberSearch() {
        CreateTeam activity = (CreateTeam) requireActivity();
        activity.getAllAssociates();
        adapter.notifyDataSetChanged();

    }

    private void closeKeyboard(View view) {
        // Coding in Flow video said this.getCurrentFocus(), but that doesn't exist :/
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
        }
        Log.w(TAG, "keyboard closed!");
    }
}