package com.example.bismapp.ui.modifyTeams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.OkCancelFragment;
import com.example.bismapp.R;
import com.example.bismapp.Team;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;
import com.example.bismapp.YourTeams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChangeMemberTeam extends AppCompatActivity {
    private OkCancelFragment okCancel;
    String method = "N/A";
    TeamMember member;
    String team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_member_team);

        okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.change_okay_cancel, okCancel).commit();

        // getting the bundle back from the android
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        method = bundle.getString("Method");
        member = (TeamMember)bundle.get("Member");
        team = bundle.getString("Team");
        printMessage();
    }

    private void printMessage() {
        String message;
        String associateName = null;
        String associateTeam = null;
        String associateStation = null;
        if (member != null) {
            associateName = member.getName();
            associateTeam = member.getTeam();
            associateStation = member.getStation();
        }
        TextView textView = findViewById(R.id.change_text);
        int bismBlue = ContextCompat.getColor(this, R.color.bism_blue);
        int black = ContextCompat.getColor(this, R.color.black);
        if (method.equals("change")) { // change member's team
            message = associateName + " is already in team " + associateTeam +
                    ".\n\nDo you want to change their team?";
            // this is the text we'll be operating on
            SpannableString text = new SpannableString(message);
            // make associate's name blue
            text.setSpan(new ForegroundColorSpan(bismBlue), 0, associateName.length(), 0);
            // rest of text black
            text.setSpan(new ForegroundColorSpan(black), associateName.length() + 1,
                    message.length(), 0);
            // team blue
            text.setSpan(new ForegroundColorSpan(bismBlue), associateName.length() + 20,
                    associateName.length() + associateTeam.length() + 20, 0);
            textView.setText(text);
        } else if (method.equals("remove")) { // Remove a member from list
            message = "Remove " + associateName + " at Station " + associateStation + "?";
            // this is the text we'll be operating on
            SpannableString text = new SpannableString(message);
            // "Remove" is black
            text.setSpan(new ForegroundColorSpan(black), 0, 6, 0);
            // make associate's name blue
            text.setSpan(new ForegroundColorSpan(bismBlue), 7, associateName.length() + 7,
                    0);
            // "at" is black
            text.setSpan(new ForegroundColorSpan(black), associateName.length() + 8,
                    associateName.length() + 19, 0);
            // station is blue
            text.setSpan(new ForegroundColorSpan(bismBlue), associateName.length() + 19,
                    associateName.length() + 20 + associateStation.length(), 0);
            // "?" is black
            text.setSpan(new ForegroundColorSpan(black),message.length() - 1,
                    message.length(), 0);
            textView.setText(text);
        } else if (method.equals("team")) {
            message = "Remove Team " + team + " and remove all team members from the team?" +
                    "\n\nThis action cannot be undone.";
            // this is the text we'll be operating on
            SpannableString text = new SpannableString(message);
            // "Remove" is black
            text.setSpan(new ForegroundColorSpan(black), 0, 6, 0);
            // make associate's name blue
            text.setSpan(new ForegroundColorSpan(bismBlue), 7, team.length() + 13,
                    0);
            // "at" is black
            text.setSpan(new ForegroundColorSpan(black), team.length() + 14,
                    message.length() - 1, 0);
            textView.setText(text);
        }
    }

    public void cancelButtonClicked() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void okButtonClicked() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
