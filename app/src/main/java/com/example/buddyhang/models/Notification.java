package com.example.buddyhang.models;

public class Notification {
    // for storing notification data
    String timestamp;
    String hostId;
    String userAcceptId;
    String eventName;

    // for rendering notification UI
    String acceptUserName;

    public Notification() {
    }

    public Notification(String timestamp, String hostId, String userAcceptId, String eventName) {
        this.timestamp = timestamp;
        this.hostId = hostId;
        this.userAcceptId = userAcceptId;
        this.eventName = eventName;
    }

    public Notification (String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public String getUserAcceptId() {
        return userAcceptId;
    }

    public void setUserAcceptId(String userAcceptId) {
        this.userAcceptId = userAcceptId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostId() {
        return hostId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
