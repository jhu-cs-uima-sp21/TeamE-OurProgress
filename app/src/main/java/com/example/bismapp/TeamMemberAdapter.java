package com.example.bismapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

import java.util.ArrayList;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.ViewHolder> {
    public ArrayList<TeamMember> teamMembers;
    private TeamMRFragment activity;

    public TeamMemberAdapter(Fragment activity, ArrayList<TeamMember> teamMembers) {
        //TODO: maybe remove below
        // LayoutInflater inflater = LayoutInflater.from(activity.getContext());
        this.teamMembers = teamMembers;
        this.activity = (TeamMRFragment) activity;
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

    public void addTeamMembers(TeamMember newTeamMember) {
        teamMembers.add(newTeamMember);
        notifyDataSetChanged();
    }

    /* TODO: figure out if need remove these unused functions
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
    }*/

    @Override
    public int getItemCount() { return teamMembers.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView id;


        public ViewHolder(View view, TextView name, TextView id) {
            super(view);
            this.name = name;
            this.id = id;

            ImageButton remove_member_btn = (ImageButton) view.findViewById(R.id.remove_member_btn);
            remove_member_btn.setOnClickListener((View.OnClickListener) btnview  -> {
                btnview.startAnimation(MainActivity.buttonClick);
                Toast toast = Toast.makeText(btnview.getContext(),
                        teamMembers.get(getAdapterPosition()).getName()
                                + " has been removed from team", Toast.LENGTH_SHORT);
                toast.show();
                try {
                    ((TeamMRFragment)activity).removeTeamMember(getAdapterPosition(),
                            name.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
// Delete things at end of TeamMRFragment?
            });
        }

        public void setData(TeamMember teamMember) {
            name.setText(teamMember.getName());
            id.setText(teamMember.getID());
        }
    }
}
