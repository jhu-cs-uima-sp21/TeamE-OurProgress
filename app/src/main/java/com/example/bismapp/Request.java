package com.example.bismapp;

public class Request {
    private String senderID, receiverID; //these fiels are necessary
    private boolean team;
    int numReq;
    //constructor for request for one individual (team member or supervisor)

    //if team is true, then receiver id isnt an id -- it's a team name
    public Request(String senderID, String receiverID, boolean team) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.team = team;
    }

    public Request(String senderID, String receiverID, boolean team, int numReq) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.team = team;
        this.numReq = numReq;
    }

    public int getNumReq() {
        return numReq;
    }

    public void setNumReq(int numReq) {
        this.numReq = numReq;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public boolean isTeam() {
        return team;
    }

    public void setTeam(boolean team) {
        this.team = team;
    }
}
