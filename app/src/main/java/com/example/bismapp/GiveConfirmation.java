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

public class GiveConfirmation extends AppCompatActivity {
    private static final String TAG = "tag?";
    private String name, senderID, receiverID;
    private boolean isTeam;
    private OkCancelFragment okCancel;
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private Integer reqNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_confirmation);
        okCancel = new OkCancelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.okay_cancel_frag, okCancel).commit();
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        reqNum = (Integer) intent.getIntExtra("REQNUM", 0);
        System.out.println("numRequest = " + reqNum);
        TextView nameView = (TextView) findViewById(R.id.requester);
        nameView.setText(name);
    }


    //THIS REMOVES REQUEST FROM FIREBASE!
    public void okButtonClicked() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbref.child("requests").child(reqNum.toString()).removeValue();
            }
            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, "You've accepted the help request!", duration);
        toast.show();

        finish();
    }

    public void cancelButtonClicked() {
        finish();
    }
}