package com.example.bismapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<TeamMember> teamMembers;

    public TeamMemberAdapter(FragmentActivity activity, ArrayList<TeamMember> teamMembers) {
        inflater = LayoutInflater.from(activity);
        this.teamMembers = teamMembers;
    }

    @NonNull
    @Override
    public TeamMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_member, parent, false);
        TextView name = (TextView)view.findViewById(R.id.member_name);
        TextView id = (TextView)view.findViewById(R.id.member_id);
        return new ViewHolder(view, name, id);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberAdapter.ViewHolder holder, int position) {
        TeamMember member = teamMembers.get(position);
        holder.setData(member);
    }

    @Override
    public int getItemCount() { return teamMembers.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewHolder(View view, TextView name, TextView id) {
            super(view);
            this.name = name;
            this.id = id;
        }

        public void setData(TeamMember teamMember) {
            name.setText(teamMember.getName());
            id.setText(String.valueOf(teamMember.getId()));
        }
    }
}
