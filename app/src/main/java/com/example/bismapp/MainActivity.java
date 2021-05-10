package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.contentcapture.DataShareRequest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private static final String TAG = "dbref: ";
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;

    private Boolean isRemembered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        peditor = myPrefs.edit();
        setContentView(R.layout.activity_main);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
        buttonClick.setDuration(100);

        Boolean remember = myPrefs.getBoolean("REMEMBER", false);
        EditText id_field = (EditText) findViewById(R.id.eid_field);
        // Clear text
        if (!remember) {
            //EditText id = (EditText) findViewById(R.id.eid_field);
            // Clear text
            id_field.setText("");
        } else {
            String id = myPrefs.getString("ID", "");
            id_field.setText(id);
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
            checkBox.setChecked(true);
        }

        Button login_btn = (Button)findViewById(R.id.login_b);
        login_btn.setOnClickListener((View.OnClickListener) view -> { // switch to Your Teams activity
            //view.startAnimation(buttonClick);
            EditText id = (EditText) findViewById(R.id.eid_field);
            String entered_id = id.getText().toString();

            System.out.println("hello, the entered ID is: " + entered_id);

            // TODO: DO we need this?
            //boolean isAssociate;

            //dbref.child("users").child("managers").child(entered_id).exists();

            //READ FROM DATABASE TO CHECK IF MANAGER
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean isManager = snapshot.child("users").child("managers").child(entered_id).exists();
                    String teamID = null;
                    boolean isAssociate = snapshot.child("users").child("associates").child(entered_id).exists();
                    if (entered_id.equals("")) {
                        isManager = false;
                        isAssociate = false;
                    }
                    System.out.println("isManager is " + isManager);
                    System.out.println("isAssociate is " + isAssociate);

                    if (isManager) {
                        Log.w(TAG, "This user is a Manager");
                        peditor.putString("ID", entered_id);
                    } else if (isAssociate) {
                        teamID = snapshot.child("users").child("associates").child(entered_id).child("team").getValue(String.class);
                        peditor.putString("ID", entered_id);
                        peditor.putString("TEAM", teamID);
                        System.out.println("the team id IS : " + teamID);
                        Log.w(TAG, "This user is a Associate");
                    }
                    peditor.apply();


                    System.out.println("Shared Prefs Manager is: " + myPrefs.getBoolean("MANAGER", false));
                    System.out.println("Shared Prefs Associate is: " + myPrefs.getBoolean("ASSOCIATE", false));

                    if (isManager) {
                        Intent intent = new Intent(getApplicationContext(), YourTeams.class);
                        startActivity(intent);
                    } else if (isAssociate && !snapshot.child("teams").child(teamID).exists()) {
                        Intent intent = new Intent(getApplicationContext(), AssociateNavigationActivity.class);
                        startActivity(intent);
                    } else if (isAssociate) {
                        Intent intent = new Intent(getApplicationContext(), AssociateNavigationActivity.class);
                        startActivity(intent);
                    } else {
                            CharSequence text = "Please enter a valid employee id";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                            Log.w(TAG, "Invalid Employee ID");
                    }




                    Log.d(TAG, "Children count: " + snapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(view -> { // switch to Your Teams activity
            view.startAnimation(MainActivity.buttonClick);
            if(!checkBox.isChecked()) {
                isRemembered = false;
            } else {
                isRemembered = true;
            }

            //TODO: Might need to uncomment if this fails!
            //myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //SharedPreferences.Editor peditor = myPrefs.edit();

            peditor.putBoolean("REMEMBER", isRemembered);
            peditor.apply();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        @Override
    protected void onResume() {
        super.onResume();

        Boolean remember = myPrefs.getBoolean("REMEMBER", false);
        if (!remember) {
            EditText id = (EditText) findViewById(R.id.eid_field);
            // Clear text
            id.setText("");
        }
    }
}
