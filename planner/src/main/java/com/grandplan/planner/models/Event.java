package com.grandplan.planner.models;

public class Event {
    private String title;
    private String date;
    private String start;
    private String end;
    private Boolean allDay;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
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
}