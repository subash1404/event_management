package com.example.eventmanagement.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeedbackRequest {
    private Long studentId;
    private Long eventId;
    private String message;
}
