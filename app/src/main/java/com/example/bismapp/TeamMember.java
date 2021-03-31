package com.example.bismapp;

public class TeamMember extends User {
    private String name;
    private String id;
    private String station = "N/A";

    public TeamMember(String name, String id, String station) {
        this.name = name;
        this.id = id;
        this.station = station;
    }

    public TeamMember(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {return name;}

    public String getId() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }
}
