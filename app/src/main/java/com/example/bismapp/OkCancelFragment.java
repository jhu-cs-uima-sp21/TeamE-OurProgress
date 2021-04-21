package com.example.bismapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OkCancelFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ok_cancel, container, false);

        ImageButton ok_btn = (ImageButton) view.findViewById(R.id.ok);
        ok_btn.setOnClickListener(btnView -> {
            btnView.startAnimation(MainActivity.buttonClick);
            if (getActivity() instanceof CreateTeam) {
                ((CreateTeam) getActivity()).okButtonClicked();
            } else if (getActivity() instanceof AskConfirmation) {
                ((AskConfirmation) getActivity()).okButtonClicked();
            }
        });

        ImageButton cancel_btn = (ImageButton) view.findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(btnView -> {
            btnView.startAnimation(MainActivity.buttonClick);
            if (getActivity() instanceof CreateTeam) {
                ((CreateTeam) getActivity()).cancelButtonClicked();
            } else if (getActivity() instanceof AskConfirmation) {
                ((AskConfirmation) getActivity()).cancelButtonClicked();
            }
        });

    return view;
    }
}
