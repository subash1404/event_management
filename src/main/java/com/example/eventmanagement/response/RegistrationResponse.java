package com.example.eventmanagement.response;

import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Type;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Registration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationResponse {
    private String studentName;
    private String email;
    private String studentCollege;
    private String eventName;
    private String eventCollege;
    private LocalDate eventDate;
    private String eventLocation;
    private Type type;
    private EventType eventType;

}
