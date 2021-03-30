package com.example.bismapp;

public class TeamMember {
    private String name;
    private final int id;
    private String station;

    TeamMember(String name, int id, String station) {
        this.name = name;
        this.id = id;
        this.station = station;
    }

    TeamMember(String name, int id) {
        this.name = name;
        this.id = id;
        this.station = "NA";
    }

    public String getName() {return name;}

    public int getId() {return id;}

    public String getStation() {return station;}
}
