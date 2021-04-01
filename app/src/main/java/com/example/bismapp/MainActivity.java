package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
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

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.contentcapture.DataShareRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    public static ArrayList<TeamMember> teamMembers = new ArrayList<>(5);
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;
    private static final String TAG = "dbref: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

        // Dummy list of team members
        for (int i = 1; i < 6; i++) {
            TeamMember member = new TeamMember("Member #" + i,
                    String.valueOf(i * 82345679 % 10000000), "Station " + (6 - i));
            teamMembers.add(member);
        }

        /*

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        buttonClick.setDuration(100);
        Button login_btn = (Button)findViewById(R.id.login_b);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                EditText id = (EditText)findViewById(R.id.eid_field);
                String entered_id = id.getText().toString();

                System.out.println("hello");

                boolean isAssociate;

                //READ FROM DATABASE TO CHECK IF MANAGER
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println("hello");
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        // do something with snapshot values
                        boolean isManager = snapshot.child("users").child("managers").child(entered_id).exists();
                        boolean isAssociate = snapshot.child("users").child("associates").child(entered_id).exists();

                        if (isManager) {
                            Log.w(TAG, "This user is a Manager");
                            Intent intent = new Intent(getApplicationContext(), YourTeams.class);
                            startActivity(intent);
                        } else if (isAssociate) {
                            Log.w(TAG, "This user is a Associate");
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            startActivity(intent);
                        } else {
                            //TODO: See if we can make the toast larger
                            Context context = getApplicationContext();
                            CharSequence text = "ERROR: Please enter a valid employee id";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Log.w(TAG, "Invalid Employee ID");
                        }


                        Log.d(TAG, "Children count: " + snapshot.getChildrenCount()); }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException()); }
                });

                /*dbref.child("users").child("managers").child("123456789").child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });*/

                //end of reading from data base

                /*Intent intent = new Intent(getApplicationContext(), YourTeams.class);
                startActivity(intent);*/
            }
        });

        // dbref.child("users").child("associates").child("234567891").exists();
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

}