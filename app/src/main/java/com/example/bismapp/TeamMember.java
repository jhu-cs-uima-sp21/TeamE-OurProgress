package com.example.bismapp;

public class TeamMember extends User {
    private String name;
    private String id;
    private String station = "N/A";
    private boolean onTeam = false;

    public TeamMember(String name, String id, String station, boolean onTeam) {
        this.name = name;
        this.id = id;
        this.station = station;
        this.onTeam = onTeam;
    }

    public TeamMember(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {return name;}

    public String getId() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }

    public boolean isOnTeam() {
        return onTeam;
    }

    public void setOnTeam(boolean onTeam) {
        this.onTeam = onTeam;
    }

}