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

    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private static final String TAG = "dbref at YourTeams: ";
    public ArrayList<String> namesAndIDs;
    public ArrayAdapter<String> adapter;
    HashSet<String> currTeamMembers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

        CreateTeam activity = (CreateTeam)requireActivity();
        currTeamMembers = activity.membersOnList;

        // do we have to remove this line bc it draws from the dummy team members
        namesAndIDs = activity.getTeamMemberNames();
        addAllIfNotNull(namesAndIDs, activity.getTeamMembersIDs());
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, namesAndIDs);
        updateMemberSearch();

        /*String[] tempTeamMembers = ((CreateTeam) requireActivity()).teamRoster
                .getTeamMemberAdapter().getTeamMemberNames();
        */
        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.enterNames);
        textView.setAdapter(adapter);
        // does touch not work with emulator, or is my code not working?
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard(view);
                return false;
            }
        });

        // clear text in member search
        ImageButton clearName_btn =(ImageButton) view.findViewById(R.id.clearName_btn);
        clearName_btn.setOnClickListener(btnView -> {
            btnView.startAnimation(MainActivity.buttonClick);
            textView.setText("");
        });

        // add a member from search
        ImageButton enterNames_btn = (ImageButton) view.findViewById(R.id.enterNames_btn);
        enterNames_btn.setOnClickListener(btnView -> {
            btnView.startAnimation(MainActivity.buttonClick);
            String newTeamMemberName = textView.getText().toString();
            try {
                if (currTeamMembers.contains(newTeamMemberName)) {
                    Toast toast = Toast.makeText(((CreateTeam)requireActivity()),
                            newTeamMemberName
                                    +" is already on the team", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                TeamMember newMember;
                try {
                    newMember = MainActivity.getTeamMember(newTeamMemberName);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(((CreateTeam)requireActivity()),
                            "Can not find member: " + newTeamMemberName, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                activity.teamRoster.getTeamMemberAdapter().addTeamMembers(newMember);
                currTeamMembers.add(newTeamMemberName);
                currTeamMembers.add(newMember.getId());
                Toast toast = Toast.makeText(((CreateTeam)requireActivity()),
                        newTeamMemberName
                                +" has been added to the team", Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) {
                System.out.println("NULL TEAM MEMBER");
            }
            closeKeyboard(view);
        });

        return view;

        // TODO: actually add member
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMemberSearch();
    }

    public void updateMemberSearch() {
        CreateTeam activity = (CreateTeam)requireActivity();
        namesAndIDs = activity.getTeamMemberNames();
        addAllIfNotNull(namesAndIDs, activity.getTeamMembersIDs());
        adapter.notifyDataSetChanged();

    }

    public static <E> void addAllIfNotNull(List<E> list, Collection<? extends E> c) {
        if (c != null && list != null) {
            list.addAll(c);
        }
    }

    private void closeKeyboard(View view) {
//        View view = this.getView();
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

/*  TODO: possibly remove unused functions
    public Set<String> getCurrTeamMembers() {
        return currTeamMembers;
    }

    public void setCurrTeamMembers(Set<String> currTeamMembers) {
        this.currTeamMembers = currTeamMembers;
    }*/

}