package com.example.eventmanagement.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.example.eventmanagement.models.Feedback;
import com.example.eventmanagement.models.Registration;
import com.example.eventmanagement.response.EventResponse;
import com.example.eventmanagement.response.FeedbackResponse;
import com.example.eventmanagement.response.RegistrationResponse;
import com.example.eventmanagement.services.FeedbackService;
import com.example.eventmanagement.services.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class EventResponseResolver implements GraphQLResolver<EventResponse> {
    public List<RegistrationResponse> getRegistrationList(EventResponse eventResponse) {
        List<RegistrationResponse> registrationResponses = new ArrayList<>();
        if(eventResponse.getEvent().getRegistrations() != null) {
            for (Registration registration : eventResponse.getEvent().getRegistrations()) {
                registrationResponses.add(RegistrationService.getRegistrationResponse(registration.getStudent(), registration.getEvent()));
            }
        }
        return registrationResponses;
    }
    public List<FeedbackResponse> getFeedbackList(EventResponse eventResponse) {
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        if(eventResponse.getEvent().getFeedbacks() != null) {
            for (Feedback feedback : eventResponse.getEvent().getFeedbacks()) {
                feedbackResponses.add(FeedbackService.getFeedbackResponse(feedback, feedback.getStudent(), feedback.getEvent()));
            }
        }
        return feedbackResponses;
    }
}