package com.example.bismapp;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamMember extends User implements Parcelable {
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

    public static final Creator<TeamMember> CREATOR = new Creator<TeamMember>() {
        @Override
        public TeamMember createFromParcel(Parcel in) {
            return new TeamMember(in);
        }

        @Override
        public TeamMember[] newArray(int size) {
            return new TeamMember[size];
        }
    };

    public String getName() {return name;}

    public String getID() {return id;}

    public String getStation() {return station;}

    public void setStation (String newStation) { this.station = newStation; }

    public String getTeam() { return team; }

    public void setTeam(String team) { this.team = team; }

    protected TeamMember(Parcel in) {
        name = in.readString();
        id = in.readString();
        station = in.readString();
        team = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(station);
        dest.writeString(team);
    }
}