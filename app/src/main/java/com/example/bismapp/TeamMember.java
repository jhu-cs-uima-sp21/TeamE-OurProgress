package com.example.bismapp;

public class TeamMember extends User {
    private String name;
    private String id;
    private String station = "N/A"; // default
    private String team = "N/A"; // default

    public TeamMember(String name, String id, String station, String team) {
        this.name = name;
        this.id = id;
        this.station = station;
        this.team = team;
    }

    public TeamMember(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {return name;}

    public String getID() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }

    /*
    public boolean isOnTeam() { return onTeam; }

    public void setOnTeam(boolean onTeam) { this.onTeam = onTeam; }
    */

    public String getTeam() { return team; }

    public void setTeam(String team) { this.team = team; }
}