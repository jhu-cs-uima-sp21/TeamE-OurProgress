package com.example.bismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private String name, id, receiverID;
    private boolean isTeam;
    private OkCancelFragment okCancel;
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;
    private Request newReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_confirmation);
        okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.okay_cancel_frag, okCancel).commit();
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        receiverID = intent.getStringExtra("RECEIVERID");
        isTeam = intent.getBooleanExtra("ISTEAM", false);
        id = myPrefs.getString("ID", "");
        newReq = new Request(id, receiverID, isTeam);

        TextView nameView = (TextView) findViewById(R.id.requester);
        TextView questionMarkView = (TextView) findViewById(R.id.question_mark);
        nameView.setText(name);
        questionMarkView.setText("?");

        if (isTeam) {
            nameView.setText("Team " + name);
        }


    }

    //THIS PUTS REQUEST IN FIREBASE!
    public void okButtonClicked() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //increment numrequests
                Integer numRequests = snapshot.child("numRequests").getValue(Integer.class);
                System.out.println(numRequests);
                if (numRequests == Integer.MAX_VALUE - 1) {
                    numRequests = 0;
                } else {
                    numRequests++;
                }
                dbref.child("numRequests").setValue(numRequests);
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