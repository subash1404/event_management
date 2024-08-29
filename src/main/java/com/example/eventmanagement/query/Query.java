package com.example.eventmanagement.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.User;
import com.example.eventmanagement.response.EventResponse;
import com.example.eventmanagement.response.UserResponse;
import com.example.eventmanagement.services.EventService;
import com.example.eventmanagement.services.RedisService;
import com.example.eventmanagement.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class Query implements GraphQLQueryResolver {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private RedisService redisService;


    public String firstQuery(){
        return "First";
    }
    public List<UserResponse> findAllUsers(){
//        List<UserResponse> cacheUsers =  redisService.getData("allUsers",new TypeReference<List<UserResponse>> (){});
        List<UserResponse> cacheUsers = redisService.getData("allUsers", new TypeReference<List<UserResponse>>() {});
        if(cacheUsers != null){
            System.out.println("Users found in cache");
            return cacheUsers;
        }
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userService.findAllUsers();
        for(User user : users){
            userResponses.add(new UserResponse(user));
        }
        redisService.saveData("allUsers",userResponses,10, TimeUnit.MINUTES);
        System.out.println("Users set in redis");
        return userResponses;
    }
    public UserResponse findUserById(Long id){
        UserResponse userResponse = redisService.getData("user_" + id, UserResponse.class);
        if(userResponse != null){
            System.out.println("User found in cache");
            return userResponse;
        }
        userResponse = new UserResponse(userService.findUserById(id));
        redisService.saveData("user_" + id, userResponse, 10, TimeUnit.MINUTES);
        System.out.println("User saved in cache");
        return userResponse;
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
