package com.example.bismapp.ui.modifyTeams;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.EditTeam;
import com.example.bismapp.R;
import com.example.bismapp.Team;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.RunnableScheduledFuture;

public class TeamMRFragment extends Fragment {
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private RecyclerView teamRoster;
    private Activity activity;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;
    private TeamMemberAdapter adapter;
    private final int LAUNCH_REMOVE_MEMBER = 2;
    private static final String TAG = "dbref at TeamMRF: ";

    // for the Intent
    private int changedIndex = -1;
    private TeamMember changedMember = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_member_roster, container, false);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        peditor = myPrefs.edit();
        activity = getActivity();
        adapter = new TeamMemberAdapter(this, new ArrayList<>());

        // setting up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        teamRoster = (RecyclerView) view.findViewById(R.id.team_member_recycler);
        teamRoster.setHasFixedSize(true);
        teamRoster.setLayoutManager(layoutManager);

        // pre-populate roster if EditTeam
        if (getActivity() instanceof EditTeam) {
            prePopulatedTeamMembers();
        }

        teamRoster.setAdapter(adapter);
        return view;
    }

    // Read from database to get list of team members
    private void prePopulatedTeamMembers() {
        String teamName =  myPrefs.getString("TEAM", "A");

        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<TeamMember> associates = new ArrayList<>();
                Iterable<DataSnapshot> teamMembers = snapshot.child("teams").child(teamName)
                        .child("team_members").getChildren();
                System.out.printf("Fetching associates from team %s...\n", teamName);
                for (DataSnapshot i : teamMembers) {
                    String userName = i.child("name").getValue(String.class);
                    String userID = i.child("id").getValue(String.class);
                    String station = i.child("station").getValue(String.class);
                    String team = i.child("team").getValue(String.class);
                    associates.add(new TeamMember(userName, userID, station, team));
                    System.out.printf("\tMember: %s,  ID: %s,  Station: %s, Team: %s\n", userName,
                            userID, station, team);
                }
                setAdapter(associates);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setAdapter(ArrayList<TeamMember> associates) {
        adapter = new TeamMemberAdapter(this, associates);
        teamRoster.setAdapter(adapter);
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return adapter.getTeamMembers();
    }

    public TeamMemberAdapter getTeamMemberAdapter() {
        return adapter;
    }

    public void checkToRemoveTeamMember(int index, TeamMember member) {
        Intent intent = new Intent(getActivity(), ChangeMemberTeam.class);
        intent.putExtra("Method", "remove");
        intent.putExtra("Member", member);
        changedIndex = index;
        changedMember = member;
        startActivityForResult(intent, LAUNCH_REMOVE_MEMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (changedMember == null) { return; }

        if (requestCode == LAUNCH_REMOVE_MEMBER) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (isFromATeam()) {
                        if (activity instanceof CreateTeam) {
                            ((CreateTeam)activity).notRemoveAssociateFromOldTeam(changedMember.getID());
                        } else { // instanceof EditTeam
                            ((EditTeam)activity).notRemoveAssociateFromOldTeam(changedMember.getID());
                        }
                    }
                    removeTeamMember(changedIndex, changedMember.getName());
                } catch (Exception e) {
                    makeToast("Error occurred: Was not able to remove associate from roster");
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                makeToast(changedMember.getName() + " was not removed from the team");
            }
        }
    }

    private boolean isFromATeam() {
        if (activity instanceof CreateTeam) {
            return ((CreateTeam)activity).associatesToTeamChange.containsKey(changedMember.getID());
        } else { // instanceof EditTeam
            return ((EditTeam)activity).associatesToTeamChange.containsKey(changedMember.getID());
        }
    }

    public void removeTeamMember(int index, String name) throws Exception {
        TeamMember member = adapter.teamMembers.get(index);
        member.setTeam("N/A");
        // Update AutoCompleteTextView adapter
        if (requireActivity() instanceof CreateTeam) {
            CreateTeam activity = (CreateTeam)requireActivity();
            activity.getAssociate(name).setTeam("N/A"); // throws exception
            activity.teamInfo.adapter.add(name);
            activity.teamInfo.adapter.notifyDataSetChanged();
        } else if (requireActivity() instanceof EditTeam) {
            EditTeam activity = (EditTeam)requireActivity();
            activity.getAssociate(name).setTeam("N/A"); // throws exception
            activity.teamInfo.adapter.add(name);
            activity.teamInfo.adapter.notifyDataSetChanged();
        }

        // Update Roster adapter
        adapter.teamMembers.remove(index);
        adapter.notifyDataSetChanged();

        makeToast(name + " has been removed from the roster");
    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}

