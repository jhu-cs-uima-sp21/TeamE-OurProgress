package com.example.bismapp;

import java.util.ArrayList;

public class Manager extends User {
    private String id;
    public ArrayList<Team> teams = new ArrayList<Team>();

    public Manager() {
    }

    public Manager(String id) {
        super(id);
    }
}