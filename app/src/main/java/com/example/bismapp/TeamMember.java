package com.example.bismapp;

public class TeamMember {
    private String name;
    private final int id;
    private String station;

    public TeamMember(String name, int id, String station) {
        this.name = name;
        this.id = id;
        this.station = station;
    }

    TeamMember(String name, int id) {
        this.name = name;
        this.id = id;
        this.station = "N/A";
    }

    public String getName() {return name;}

    public int getId() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }
}
