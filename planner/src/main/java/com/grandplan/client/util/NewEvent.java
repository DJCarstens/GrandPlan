package com.grandplan.client.util;

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
    private boolean allDay;
    private String tag;
    private String color;
}