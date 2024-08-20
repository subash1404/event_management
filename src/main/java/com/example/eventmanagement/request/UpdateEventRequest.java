package com.example.eventmanagement.request;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventRequest {
    String name;
    String date;
    String location;
    Long collegeId;
    Integer maxRegistrationLimit;
    Type type;
    EventType eventType;
}
