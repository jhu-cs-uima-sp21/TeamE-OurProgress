package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.MainActivity;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TeamInfoFragment extends Fragment {

    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private static final String TAG = "dbref at YourTeams: ";
    public List<String> namesAndIDs;
    public ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

        CreateTeam activity = (CreateTeam)requireActivity();
        namesAndIDs = activity.getTeamMemberNames();
        addAllIfNotNull(namesAndIDs, activity.getTeamMembersIDs());
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, namesAndIDs);

        updateMemberSearch();

        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.enterNames);

        textView.setAdapter(adapter);

        ImageButton enterNames_btn = (ImageButton) view.findViewById(R.id.enterNames_btn);
        enterNames_btn.setOnClickListener(btnView -> {
            btnView.startAnimation(MainActivity.buttonClick);
            String newTeamMemberName = textView.getText().toString();
            try {
                TeamMember newMember = MainActivity.getTeamMember(newTeamMemberName);
                ((CreateTeam) requireActivity()).teamRoster.getTeamMemberAdapter().addTeamMembers(newMember);
            } catch (Exception e) {
                System.out.println("NULL TEAM MEMBER");
            }
            Toast toast = Toast.makeText(((CreateTeam)requireActivity()),
                    newTeamMemberName
                            +" has been added to the team", Toast.LENGTH_SHORT);
            toast.show();
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
}