package com.example.eventmanagement.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.eventmanagement.models.*;
import com.example.eventmanagement.request.*;
import com.example.eventmanagement.response.EventResponse;
import com.example.eventmanagement.response.FeedbackResponse;
import com.example.eventmanagement.response.RegistrationResponse;
import com.example.eventmanagement.response.UserResponse;
import com.example.eventmanagement.services.EventService;
import com.example.eventmanagement.services.FeedbackService;
import com.example.eventmanagement.services.RegistrationService;
import com.example.eventmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class Mutation implements GraphQLMutationResolver {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private FeedbackService feedbackService;

    public UserResponse updateUser(UpdateUserRequest updateUserRequest){
        return userService.updateUser(updateUserRequest);
    }

    public String deleteUser(Long userId){
        return userService.deleteUser(userId);
    }
    public EventResponse createEvent(CreateEventRequest createEventRequest){
        return eventService.createEvent(createEventRequest);
    }
    public EventResponse updateEvent(UpdateEventRequest updateEventRequest){
        return eventService.updateEvent(updateEventRequest);
    }
    public String deleteEventById(Long id){
        return eventService.deleteEventById(id);
    }
    public String deleteEventByCollegeAndName(Long collegeId,String name){
        return eventService.deleteByCollegeAndName(collegeId,name);
    }
    public RegistrationResponse registerEvent(Long studentId, Long eventId){
        return registrationService.registerEvent(studentId,eventId);
    }
    public String deleteRegistration(Long studentId,Long eventId){
        registrationService.deleteRegistration(studentId,eventId);
        return "Registration for the event deleted successfully";
    }
    public FeedbackResponse postFeedback(CreateFeedbackRequest createFeedbackRequest){
        return feedbackService.postFeedback(createFeedbackRequest);
    }
    public String deleteFeedback(Long feedbackId){
        feedbackService.deleteFeedback(feedbackId);
        return "Feedback deleted";
    }
}
