package com.example.eventmanagement.response;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.Feedback;
import com.example.eventmanagement.models.Registration;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EventResponse {
    // For dev purposes, don't include it in schema
    private Event event;

    private String name;
    private LocalDate date;
    private String location;
    private Integer maxRegistrationLimit;
    private Integer currentRegistrations;
    private College college;
    private Type type;
    private EventType eventType;
    private List<RegistrationResponse> registrationList;
    private List<FeedbackResponse> feedbackList;
}
