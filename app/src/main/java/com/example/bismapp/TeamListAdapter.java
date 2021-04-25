package com.example.bismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

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
        int realPosition = position + 1;
        Team team = teams.get(position);
        holder.setData(team);
        holder.itemView.setContentDescription("Team " + realPosition + ": " +
                team.getName());
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
            int percent = (int) ((double) team.getUnits_produced()
                    / team.getDaily_goal() * 100);
            if (team.getDaily_goal() > 0) {
                progress.setSecondaryProgress(percent);
            } else {
                progress.setSecondaryProgress(100);
            }
            name.setText(team.getName());
            Drawable draw;
            if (percent < 10) {
                draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_red);
            } else if (percent < 24) {
                draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_orange);
            } else if (percent < 75) {
                draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_yellow);
            } else  {
                draw = cntx.getResources().getDrawable(R.drawable.circular_progress_bar_green);
            }

            progress.setProgressDrawable(draw);
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(MainActivity.buttonClick);
            clickListener.onItemClick(getAdapterPosition(), v);
            SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.putString("TEAM", (String) name.getText());
            peditor.apply();
            //TODO is this broken?
            Intent intent = new Intent(cntx.getApplicationContext(), NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cntx.startActivity(intent);

        }
    }

    public void updateDataSet(ArrayList<Team> data) {
        // clear old list
        teams.clear();

        teams.addAll(data);

        // notify adapter
        notifyDataSetChanged();
    }


}
