/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yourname.volunteer.adminma;

/**
 *
 * @author leduyduong
 */


public class Activity {
    private int id;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private String roles;
    private int capacity;
    private String organization;
    private String contact;
    private String status;
    private String updatestatus;
    private String mapsLink;
        private double latitude;
private double longitude;

public double getLatitude() { return latitude; }
public void setLatitude(double latitude) { this.latitude = latitude; }

public double getLongitude() { return longitude; }
public void setLongitude(double longitude) { this.longitude = longitude; }


public String getMapsLink() {
    return mapsLink;
}

public void setMapsLink(String mapsLink) {
    this.mapsLink = mapsLink;
}


public String getUpdatestatus() {
    return updatestatus;
}

public void setUpdatestatus(String updatestatus) {
    this.updatestatus = updatestatus;
}


    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


