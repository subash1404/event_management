package com.example.eventmanagement.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FeedbackResponse {
    private String studentName;
    private String studentCollegeName;
    private String email;
    private String eventName;
    private String eventCollege;
    private LocalDate eventDate;
    private String message;
}
