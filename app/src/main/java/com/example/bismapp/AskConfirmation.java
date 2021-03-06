package com.example.bismapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AskConfirmation extends AppCompatActivity {
    private static final String TAG = "tag?";
    private DatabaseReference dbref;
    private Request newReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_confirmation);
        OkCancelFragment okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.okay_cancel_frag, okCancel).commit();
        FirebaseDatabase mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String receiverID = intent.getStringExtra("RECEIVERID");
        boolean isTeam = intent.getBooleanExtra("ISTEAM", false);
        String id = myPrefs.getString("ID", "");
        newReq = new Request(id, receiverID, isTeam);

        TextView nameView = (TextView) findViewById(R.id.requester);

        int bismBlue = ContextCompat.getColor(this, R.color.bism_blue);
        int black = ContextCompat.getColor(this, R.color.black);
        String textName = name;
        if (isTeam) {
            textName = "Team " + name;
        }
        String message = textName + "?";
        // this is the text we'll be operating on
        SpannableString text = new SpannableString(message);
        // make associate's name blue
        text.setSpan(new ForegroundColorSpan(bismBlue), 0, textName.length() - 1, 0);
        text.setSpan(new ForegroundColorSpan(black), textName.length(), message.length(), 0);
        nameView.setText(text);
    }

    //THIS PUTS REQUEST IN FIREBASE!
    public void okButtonClicked() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                //increment numrequests
                Integer numRequests = snapshot.child("numRequests").getValue(Integer.class);
                System.out.println(numRequests);
                if (numRequests == Integer.MAX_VALUE - 1) {
                    numRequests = 0;
                } else {
                    numRequests++;
                }
                dbref.child("numRequests").setValue(numRequests);
                newReq.setNumReq(numRequests);
                dbref.child("requests").child(numRequests.toString()).setValue(newReq);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, "Request has been sent!", duration);
        toast.show();

        finish();
    }

    public void cancelButtonClicked() {
        finish();
    }
}