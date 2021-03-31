package com.example.bismapp;

public class TeamMember extends User {
    private String name;
    private int id;
    private String station;

    public TeamMember(){

    }

    public TeamMember(String name, int id, String station) {
//        super(name);
        this.name = name;
        this.station = station;
    }

    public TeamMember(String name, int id) {
        this.name = name;
        this.id = id;
//        System.out.println(id);
        this.station = "N/A";
    }

    public String getName() {return name;}

    public int getId() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }
}
