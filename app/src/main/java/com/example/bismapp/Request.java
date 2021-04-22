package com.example.bismapp;

public class Request {
    private String senderID, recieverID; //these fiels are necessary
    private boolean isTeam;
    //constructor for request for one individual (team member or supervisor)

    //if team is true, then receiver id isnt an id -- it's a team name
    public Request(String senderID, String recieverID, boolean isTeam) {
        this.senderID = senderID;
        this.recieverID = recieverID;
        this.isTeam = isTeam;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }

    public boolean isTeam() {
        return isTeam;
    }

    public void setTeam(boolean team) {
        isTeam = team;
    }
}
