package com.grandplan.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private String title;
    private String start;
    private String end;
    private Boolean allDay;
    private String color;
    private String type;
    private String description;
}