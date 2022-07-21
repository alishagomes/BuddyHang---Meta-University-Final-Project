package com.example.buddyhang.models;

import java.util.Date;
/**
 * Represents an event post on BuddyHang
 */
public class PrivateEvent {

    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventHost;
    private String eventLocation;
    private String eventDate;
    private String eventTime;

    public PrivateEvent() {
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public PrivateEvent(String eventId, String eventName, String eventDescription, String eventHost, String eventLocation, String eventDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventHost = eventHost;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public void setVisibility(int visible) {

    }


}
