package com.example.bismapp;

public class Team {
    private String id;
    public String managed_by;

    public Team() {

    }

    public Team(String id, String managed_by) {
        this.id = id;
        this.managed_by = managed_by;
    }
}
