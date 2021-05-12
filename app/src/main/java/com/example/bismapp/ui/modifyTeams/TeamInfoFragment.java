package com.example.bismapp.ui.modifyTeams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.EditTeam;
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
    public ArrayAdapter<String> adapter;
    private final int LAUNCH_CHANGE_TEAM = 1;
    private TeamMember changedMember = null;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        peditor = myPrefs.edit();

        // Get String list of associate names
        if (getActivity() instanceof CreateTeam) {
            CreateTeam activity = (CreateTeam)getActivity();
            activity.getAllAssociates();
            adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.hint_item, activity.associatesNames);
        } else if (getActivity() instanceof EditTeam) {
            EditTeam activity = (EditTeam)getActivity();
            activity.getAllAssociates();
            adapter = new ArrayAdapter<String>(getContext(),
                     R.layout.hint_item, activity.associatesNames);
            // pre-populate goal
            EditText editGoal = (EditText)view.findViewById(R.id.enterDailyGoal);
            editGoal.setText(String.valueOf(myPrefs.getInt("DAILY_GOAL", 1000)));
        }

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
                Activity activity = getActivity();
                try {
                    if (activity instanceof CreateTeam) {
                        newMember = ((CreateTeam)activity).getAssociate(newTeamMemberName);
                    } else { // EditTeam activity
                        newMember = ((EditTeam)activity).getAssociate(newTeamMemberName);
                    }
                    newTeamMemberTeam = newMember.getTeam();
                    if (activity instanceof EditTeam) {
                        String teamName = ((EditText)activity.findViewById(R.id.edit_team_name))
                                .getText().toString().substring("Edit Team ".length());
                        if (newTeamMemberTeam.equals(teamName)) {
                            makeToast(newTeamMemberName + " is already on this team");
                            textView.setText("");
                            return;
                        }
                    }
                    if (newTeamMemberTeam.equals("TBD")) {
                        makeToast(newTeamMemberName + " has already been added to this team");
                    } else {
                        if (!newTeamMemberTeam.equals("N/A")) { // change the team member's team
                            Intent intent = new Intent(getActivity(), ChangeMemberTeam.class);
                            intent.putExtra("Method", "change");
                            intent.putExtra("Member", newMember);
                            changedMember = newMember;
                            startActivityForResult(intent, LAUNCH_CHANGE_TEAM);
                        } else {
                            newMember.setTeam("TBD");
                            adapter.remove(newMember.getName());
                            adapter.notifyDataSetChanged();
                            // add member to roster
                            if (activity instanceof CreateTeam) {
                                ((CreateTeam)activity).teamRoster.getTeamMemberAdapter()
                                        .addTeamMembers(newMember);
                            } else { // EditTeam activity
                                ((EditTeam)activity).teamRoster.getTeamMemberAdapter()
                                        .addTeamMembers(newMember);
                            }
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
        Toast toast = Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = getActivity();
        if (changedMember == null) { return; }

        if (requestCode == LAUNCH_CHANGE_TEAM) {
            if (resultCode == Activity.RESULT_OK) {
                if (activity instanceof CreateTeam) {
                    ((CreateTeam)activity).removeAssociateFromOldTeam(changedMember);
                } else { // instanceof EditTeam
                    ((EditTeam)activity).removeAssociateFromOldTeam(changedMember);
                }
                changedMember.setTeam("TBD");
                adapter.remove(changedMember.getName());
                adapter.notifyDataSetChanged();
                // add member to roster
                if (activity instanceof CreateTeam) {
                    ((CreateTeam)activity).teamRoster.getTeamMemberAdapter()
                            .addTeamMembers(changedMember);
                } else {
                    ((EditTeam)activity).teamRoster.getTeamMemberAdapter()
                            .addTeamMembers(changedMember);
                }
                makeToast(changedMember.getName() + " has been added to the team");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                makeToast(changedMember.getName() + " was not added to the team");
            }
        }
    }
}