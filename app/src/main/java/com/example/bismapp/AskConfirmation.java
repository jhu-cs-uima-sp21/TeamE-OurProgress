package com.example.bismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AskConfirmation extends AppCompatActivity {
    private static final String TAG = "tag?";
    private String name, recieverID;
    private boolean isTeam;
    private OkCancelFragment okCancel;
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_confirmation);
        okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.okay_cancel_frag, okCancel).commit();
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        recieverID = intent.getStringExtra("RECIEVERID");
        isTeam = intent.getBooleanExtra("ISTEAM", false);

        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(name);
    }

    public void okButtonClicked() {
        //TODO: Open request + push notification
        // Fetch all associates
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // do something with snapshot values

                //increment numrequests
                Integer numRequests = snapshot.child("numRequests").getValue(Integer.class);
                System.out.println(numRequests);
                numRequests = numRequests++;
                dbref.child("numRequests").setValue(numRequests);

                Request newReq = new Request(myPrefs.getString("ID", ""), recieverID, isTeam);
                dbref.child("requests").child(numRequests.toString()).setValue(newReq);
            }


            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void cancelButtonClicked() {
        finish();
    }
}