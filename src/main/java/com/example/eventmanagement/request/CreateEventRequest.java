package com.example.eventmanagement.request;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEventRequest {
    private String name;
    private String date;
    private String location;
    private Integer maxRegistrationLimit;
    private Long collegeId;
    private Long adminId;
    private Type type;
    private EventType eventType;
}
