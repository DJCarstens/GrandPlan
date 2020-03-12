package com.grandplan.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEvent{
    private String title;
    private String description;
    private String members;
    private String start;
    private String end;
    private Boolean allDay;
    private String tag;
    private String color;
}