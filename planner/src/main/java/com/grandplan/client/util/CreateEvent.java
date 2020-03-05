package com.grandplan.client.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEvent{
   @NotNull(message = "Please provide a title")
   @NotEmpty(message = "Please provide a title")
   private String title;
   private String description;
   private String[] members;

   @NotNull(message = "Please provide a start date and time")
   @NotEmpty(message = "Please provide a start date and time")
   private String start;

   @NotNull(message = "Please provide an end date and time")
   @NotEmpty(message = "Please provide an end date and time")
   private String end;
   private boolean allDay;
   private String tag;
   private String color;

}