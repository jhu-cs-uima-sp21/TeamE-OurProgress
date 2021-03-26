package com.example.bismapp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class YourTeams extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_teams);

        ImageButton refresh_btn = (ImageButton) findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(MainActivity.buttonClick);
                // TODO
            }
        });

        ImageButton create_btn = (ImageButton) findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(MainActivity.buttonClick);
                // TODO
            }
        });
    }
}
