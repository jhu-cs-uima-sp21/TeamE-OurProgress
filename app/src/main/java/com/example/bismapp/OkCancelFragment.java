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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OkCancelFragment extends Fragment {

    FirebaseDatabase mdbase;
    DatabaseReference dbref;
    private static final String TAG = "dbref: ";
    private SharedPreferences myPrefs;
    private String teamID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        View view = inflater.inflate(R.layout.fragment_ok_cancel, container, false);

        myPrefs = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        teamID = myPrefs.getString("ID", null);

        ImageButton ok_btn = (ImageButton) view.findViewById(R.id.ok);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(MainActivity.buttonClick);
                //EditText id = (EditText) findViewById(R.id.eid_field);
                //String entered_id = id.getText().toString();

                Team dummyTeam = new Team("Team Tres", "Chiam", 42,
                        -9999, null);
                // TODO add better comment

                if(getActivity() instanceof CreateTeam) {
                    Team team = new Team((view.findViewById(R.id.create_teams)).toString(),
                            teamID, 0,
                            Integer.parseInt(view.findViewById(R.id.enterDailyGoal).toString()),
                            ((CreateTeam) getActivity()).getTeamRoster().getTeamMembers());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            dbref.child("teams").child(team.getID()).setValue(team);

                            Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }
        });

    return view;
    }
}
