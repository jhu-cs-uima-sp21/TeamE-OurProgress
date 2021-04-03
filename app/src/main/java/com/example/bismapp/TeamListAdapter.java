package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
    private final ArrayList<Team> teams;
    private static ClickListener clickListener;
    private static Context cntx;


    public TeamListAdapter(ArrayList<Team> teams, Context cntx) {
        this.teams = teams;
        this.cntx = cntx;
    }

    @NonNull
    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team, parent, false);
        ProgressBar progress = view.findViewById(R.id.progressBar);
        TextView name = view.findViewById(R.id.team_name);
        return new ViewHolder(view, progress, name);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListAdapter.ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.setData(team);
    }

    @Override
    public int getItemCount() { return teams.size(); }

    /** Clickable implementation **/
    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TeamListAdapter.clickListener = clickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProgressBar progress;
        private final TextView name;

        public ViewHolder(View view, ProgressBar progress, TextView name) {
            super(view);
            this.progress = progress;
            this.name = name;
            // make view clickable
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        public void setData(Team team) {
            if (team.getDaily_goal() > 0) {
                progress.setSecondaryProgress((int) ((double) team.getUnits_produced()
                        / team.getDaily_goal() * 100));
            } else {
                progress.setSecondaryProgress(100);
            }
            name.setText(team.getName());
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(MainActivity.buttonClick);
            clickListener.onItemClick(getAdapterPosition(), v);
            Intent intent = new Intent(cntx, NavigationActivity.class);
            cntx.startActivity(intent);
        }
    }


}
