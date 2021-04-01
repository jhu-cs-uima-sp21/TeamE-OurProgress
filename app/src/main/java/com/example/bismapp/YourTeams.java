package com.example.bismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class YourTeams extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_teams);

        ImageButton create_btn = (ImageButton) findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(MainActivity.buttonClick);
                Intent intent = new Intent(getApplicationContext(), CreateTeam.class);
                startActivity(intent);
            }
        });
    }
}
