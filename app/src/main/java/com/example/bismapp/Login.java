package com.example.bismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        buttonClick.setDuration(100);
        Button login_btn = (Button)findViewById(R.id.login_b);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // switch to Your Teams activity
                view.startAnimation(buttonClick);
                EditText id = (EditText)findViewById(R.id.eid_field);
                System.out.println(id.getText().toString());
                if (id.getText().toString().equals("123456789")) {
                    Intent intent = new Intent(getApplicationContext(), YourTeams.class);
                    startActivity(intent);
                }
            }
        });
    }
}