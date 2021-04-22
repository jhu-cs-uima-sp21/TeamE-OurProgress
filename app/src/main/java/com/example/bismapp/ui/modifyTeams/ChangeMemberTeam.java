package com.example.bismapp.ui.modifyTeams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.CreateTeam;
import com.example.bismapp.OkCancelFragment;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;

import java.util.ArrayList;

public class ChangeMemberTeam extends AppCompatActivity {
    private OkCancelFragment okCancel;
    private static boolean memberChangedTeam = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_member_team);
        memberChangedTeam = false;

        okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.change_okay_cancel, okCancel).commit();

        // getting the bundle back from the android
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String associateName = "N/A";
        String associateTeam = "N/A";
        String associateStation = "N/A";
        if (intent.hasExtra("Name")) {
            associateName = bundle.getString("Name");
        }
        if (intent.hasExtra("Team")) {
            associateTeam = bundle.getString("Team");
        }
        if (intent.hasExtra("Station")) {
            associateStation = bundle.getString("Station");
        }

        String message;
        TextView textView = findViewById(R.id.change_text);
        int bismBlue = ContextCompat.getColor(this, R.color.bism_blue);
        int black = ContextCompat.getColor(this, R.color.black);
        if (bundle.getString("Method").equals("change")) {
            message = associateName + " is already in team " + associateTeam +
                    ". Do you want to change their team?";
            // this is the text we'll be operating on
            SpannableString text = new SpannableString(message);
            // make associate's name blue
            text.setSpan(new ForegroundColorSpan(bismBlue), 0, associateName.length(), 0);
            // rest of text black
            text.setSpan(new ForegroundColorSpan(black), associateName.length() + 1, message.length(), 0);
            textView.setText(text);
        } else {
            message = "Remove " + associateName + " at " + associateStation + "?";
            // this is the text we'll be operating on
            SpannableString text = new SpannableString(message);
            // "Remove" is black
            text.setSpan(new ForegroundColorSpan(black), 0, 6, 0);
            // make associate's name blue
            text.setSpan(new ForegroundColorSpan(bismBlue), 7, associateName.length() + 7, 0);
            // "at" is black
            text.setSpan(new ForegroundColorSpan(black), associateName.length() + 8,
                    associateName.length() + 10, 0);
            // station is blue
            text.setSpan(new ForegroundColorSpan(bismBlue), associateName.length() + 11,
                    associateName.length() + 11 + associateStation.length(), 0);
            // "?" is black
            text.setSpan(new ForegroundColorSpan(black), associateName.length() + associateStation.length() + 12,
                    message.length(), 0);
            textView.setText(text);
        }
    }

    public static boolean didMemberChangeTeam() { return memberChangedTeam; }
}
