package com.example.bismapp.ui.modifyTeams;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
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
    private static final String TAG = "dbref at YourTeams: ";
    private CreateTeam activity;
    public ArrayAdapter<String> adapter;
    private final int LAUNCH_CHANGE_TEAM = 1;
    private TeamMember changedMember = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        activity = (CreateTeam) requireActivity();

        // Get String list of associate names
        activity.getAllAssociates();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, activity.associatesNames);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.enterNames);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeKeyboard(textView);
                String newTeamMemberName = textView.getText().toString();
                TeamMember newMember;
                String newTeamMemberTeam;
                try {
                    newMember = activity.getAssociate(newTeamMemberName);
                    newTeamMemberTeam = newMember.getTeam();
                    if (newTeamMemberTeam.equals("TBD")) {
                        makeToast(newTeamMemberName + " has already been added to this team");
                    } else {
                        if (!newTeamMemberTeam.equals("N/A")) { // change the team member's team
                            Intent intent = new Intent(getActivity(), ChangeMemberTeam.class);
                            intent.putExtra("Name", newTeamMemberName);
                            intent.putExtra("Team", newTeamMemberTeam);
                            intent.putExtra("Method", "change");
                            changedMember = newMember;
                            startActivityForResult(intent, LAUNCH_CHANGE_TEAM);
                        } else {
                            newMember.setTeam("TBD");
                            adapter.remove(newMember.getName());
                            adapter.notifyDataSetChanged();
                            // add member to roster
                            activity.teamRoster.getTeamMemberAdapter().addTeamMembers(newMember);
                            makeToast(newTeamMemberName + " has been added to the team");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("NULL TEAM MEMBER");
                    makeToast("Can not find member, " + newTeamMemberName);
                }
                textView.setText("");
            }
        });
        return view;
    }

    private void closeKeyboard(View view) {
        // Coding in Flow video said this.getCurrentFocus(), but that doesn't exist :/
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Log.w(TAG, "keyboard closed!");
    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (changedMember == null) { return; }

        if (requestCode == LAUNCH_CHANGE_TEAM) {
            if (resultCode == Activity.RESULT_OK) {
                changedMember.setTeam("TBD");
                adapter.remove(changedMember.getName());
                adapter.notifyDataSetChanged();
                // add member to roster
                activity.teamRoster.getTeamMemberAdapter().addTeamMembers(changedMember);
                makeToast(changedMember.getName() + " has been added to the team");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                makeToast(changedMember.getName() + " was not added to the team");
            }
        }
    }
}