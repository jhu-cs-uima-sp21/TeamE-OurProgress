package com.example.bismapp;

import java.util.ArrayList;

public class Team {

    private String id;
    public String managed_by;
    public int units_produced, daily_goal = -1;
    public ArrayList<TeamMember> team_members = new ArrayList<>();

    public Team(String id, String managed_by) {
        this.id = id;
        this.managed_by = managed_by;
    }

    public Team(String id, String managed_by, int units_produced, int daily_goal,
                ArrayList<TeamMember> team_members) {
        this.id = id;
        this.managed_by = managed_by;
        this.units_produced = units_produced;
        this.daily_goal = daily_goal;
        this.team_members = team_members;
    }

    public String getID() {
        return id;
    }

    public String getManaged_by() {
        return managed_by;
    }

    public int getUnits_produced() {
        return units_produced;
    }

    public int getDaily_goal() {
        return daily_goal;
    }

    public ArrayList<TeamMember> getTeam_members() {
        return team_members;
    }

    public void setUnits_produced(int units_produced) {
        this.units_produced = units_produced;
    }

    public void setDaily_goal(int daily_goal) {
        this.daily_goal = daily_goal;
    }

    public void addMember(String id){

    }

    public void deleteMember(String id){

    }

}
