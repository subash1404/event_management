package com.example.eventmanagement.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.User;
import com.example.eventmanagement.response.EventResponse;
import com.example.eventmanagement.response.UserResponse;
import com.example.eventmanagement.services.EventService;
import com.example.eventmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class Query implements GraphQLQueryResolver {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    public String firstQuery(){
        return "First";
    }
    public List<UserResponse> findAllUsers(){
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userService.findAllUsers();
        for(User user : users){
            userResponses.add(new UserResponse(user));
        }
        return userResponses;
    }
    public UserResponse findUserById(Long id){
        return new UserResponse(userService.findUserById(id));
    }
    public Set<EventResponse> findEvents(Long userId){
        return eventService.findEvents(userId);
    }
    public EventResponse findEventById(Long id) { return eventService.findEventById(id);    }
    public Set<EventResponse> findEventByCollege(Long collegeId,Long studentId) {
        return eventService.findByCollege(collegeId,studentId);
    }
    public Set<EventResponse> findEventByLocation(String location,Long studentId) {
        return eventService.findByLocation(location,studentId);
    }
    public Set<EventResponse> findEventByDate(String date,Long studentId) {
        return eventService.findByDate(date,studentId);
    }
    public Set<EventResponse> findEventByName(String name,Long studentId){
        return eventService.findByName(name,studentId);
    }
    public Set<EventResponse> findEventByEventType(EventType eventType, Long studentId) { return eventService.findByEventType(eventType,studentId); }
    public EventResponse findEventByCollegeAndName(College college, String name) {
        return eventService.findByCollegeAndName(college,name);
    }
}
