package com.grandplan.planner.models;

public class Event {
    private String title;
    private String start;
    private String end;
    private Boolean allDay;
    private String color;
    private String type;
    private String description;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String date) {
        this.start = date;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String date) {
        this.end = date;
    }
    
    public Boolean getAllDay() {
        return this.allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }
    
    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}