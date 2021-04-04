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
    public ArrayList<TeamMember> teamMembers;
    private static ClickListener clickListener;

    public TeamMemberAdapter(FragmentActivity activity, ArrayList<TeamMember> teamMembers) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        this.teamMembers = teamMembers;
    }

    @NonNull
    @Override
    public TeamMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_member, parent, false);
        TextView name = view.findViewById(R.id.member_name);
        TextView id = view.findViewById(R.id.member_id);
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

    public String[] getTeamMemberNames() {
        String[] names = new String[teamMembers.size()];
        for (int i = 0; i < teamMembers.size(); i++) {
            names[i] = teamMembers.get(i).getName();
        }
        return names;
    }

    public String[] getTeamMemberIDs() {
        String[] ids = new String[teamMembers.size()];
        for (int i = 0; i < teamMembers.size(); i++) {
            ids[i] = teamMembers.get(i).getId();
        }
        return ids;
    }

    @Override
    public int getItemCount() { return teamMembers.size(); }

    /** Clickable implementation **/
    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TeamMemberAdapter.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView id;

        public ViewHolder(View view, TextView name, TextView id) {
            super(view);
            this.name = name;
            this.id = id;
            // make view clickable
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        public void setData(TeamMember teamMember) {
            name.setText(teamMember.getName());
            id.setText(teamMember.getId());
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(MainActivity.buttonClick);
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
