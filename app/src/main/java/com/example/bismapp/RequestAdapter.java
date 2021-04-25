package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    public ArrayList<Request> requests;
    private static Context cntx;
    private static final String TAG = "tag";
    private FirebaseDatabase mdbase;
    private DatabaseReference dbref;



    public RequestAdapter(ArrayList<Request> requests, Context cntx) {
        this.requests = requests;
        this.cntx = cntx;
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        TextView name = view.findViewById(R.id.member_name);
        TextView station = view.findViewById(R.id.station);
        return new ViewHolder(view, name, station);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.setData(request);
    }


    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        requests.add(request);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return requests.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView station;
        private Request req;
        private String sname;

        public ViewHolder(View view, TextView name, TextView station) {
            super(view);
            this.name = name;
            this.station = station;

            ImageButton remove_request_btn = (ImageButton) view.findViewById(R.id.remove_request_btn);
            remove_request_btn.setOnClickListener((View.OnClickListener) btnview  -> {
                btnview.startAnimation(MainActivity.buttonClick);
                Intent intent = new Intent(cntx.getApplicationContext(), GiveConfirmation.class);
                intent.putExtra("NAME", sname);
                intent.putExtra("RECEIVERID", req.getReceiverID());
                intent.putExtra("ISTEAM", req.isTeam());
                cntx.startActivity(intent);

                //try {
                //    activity.checkToRemoveTeamMember(getAdapterPosition(), member);
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}
            });

        }

        public void setData(Request request) {
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Iterable<DataSnapshot> usersShots = snapshot.child("users").child("associates").getChildren();
                    for (DataSnapshot i : usersShots) {
                        if (i.child("id").getValue(String.class).equals(request.getSenderID())) {
                            sname = i.child("Name").getValue(String.class);
                            name.setText(sname);
                            station.setText(i.child("team").getValue(String.class));
                        }
                    }
                    usersShots = snapshot.child("users").child("managers").getChildren();
                    for (DataSnapshot i : usersShots) {
                        if (i.child("id").getValue(String.class).equals(request.getSenderID())) {
                            sname = i.child("Name").getValue(String.class);
                            name.setText(sname);
                            station.setText(i.child("id").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
            req = request;
        }

    }

}
