package com.example.bismapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

import java.util.ArrayList;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.ViewHolder> {
    public ArrayList<TeamMember> teamMembers;
    private TeamMRFragment activity;

    public TeamMemberAdapter(Fragment activity, ArrayList<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
        this.activity = (TeamMRFragment) activity;
    }

    @NonNull
    @Override
    public TeamMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_member, parent, false);
        TextView name = view.findViewById(R.id.member_name);
        TextView id = view.findViewById(R.id.station);
        return new ViewHolder(view, name, id);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberAdapter.ViewHolder holder, int position) {
        TeamMember member = teamMembers.get(position);
        holder.setData(member);
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void addTeamMembers(TeamMember newTeamMember) {
        teamMembers.add(newTeamMember);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return teamMembers.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView id;
        private TeamMember member;

        public ViewHolder(View view, TextView name, TextView id) {
            super(view);
            this.name = name;
            this.id = id;

            ImageButton remove_member_btn = (ImageButton) view.findViewById(R.id.remove_member_btn);
            remove_member_btn.setOnClickListener((View.OnClickListener) btnview  -> {
                btnview.startAnimation(MainActivity.buttonClick);
                try {
                    activity.checkToRemoveTeamMember(getAdapterPosition(), member);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        public void setData(TeamMember teamMember) {
            name.setText(teamMember.getName());
            id.setText(teamMember.getID());
            member = teamMember;
        }
    }
}
