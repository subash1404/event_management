package com.example.eventmanagement.services;


import com.example.eventmanagement.enums.EventType;
import com.example.eventmanagement.enums.Role;
import com.example.eventmanagement.enums.Type;
import com.example.eventmanagement.models.*;
import com.example.eventmanagement.repositories.*;
import com.example.eventmanagement.request.CreateEventRequest;
import com.example.eventmanagement.request.UpdateEventRequest;
import com.example.eventmanagement.response.EventResponse;
import com.example.eventmanagement.response.FeedbackResponse;
import com.example.eventmanagement.response.RegistrationResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;

    @NotNull
    private static EventResponse getEventResponse(Event event, LocalDate parsedDate, College college) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setEvent(event);
        eventResponse.setName(event.getName());
        eventResponse.setDate(parsedDate);
        eventResponse.setLocation(event.getLocation());
        eventResponse.setMaxRegistrationLimit(event.getMaxRegistrationLimit());
        eventResponse.setType(event.getType());
        eventResponse.setEventType(event.getEventType());
        eventResponse.setCurrentRegistrations(event.getCurrentRegistrations());
        eventResponse.setCollege(college);
        return eventResponse;
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    private static Set<EventResponse> filterEvents(Set<Event> events,Student student){
        Set<EventResponse> eventResponses = new HashSet<>();
        for(Event event : events) {
            if(event.getType().equals(Type.Intra_Event) && !(event.getCollege().getName().equals(student.getCollege().getName()))){
                events.remove(event);
            }else{
                eventResponses.add(getEventResponse(event,event.getDate(),event.getCollege()));
            }
        }
        return eventResponses;
    }

    @Transactional
    @PreAuthorize("hasRole('Admin')")
    public EventResponse createEvent(CreateEventRequest createEventRequest) {
        LocalDate parsedDate = LocalDate.parse(createEventRequest.getDate());
        Event event = new Event();
        event.setName(createEventRequest.getName());
        event.setDate(parsedDate);
        event.setLocation(createEventRequest.getLocation());
        event.setMaxRegistrationLimit(createEventRequest.getMaxRegistrationLimit());
        event.setType(createEventRequest.getType());
        event.setEventType(createEventRequest.getEventType());
        College college = collegeRepository.findById(createEventRequest.getCollegeId()).orElseThrow(() -> new RuntimeException("College Not Found"));
        Admin admin = adminRepository.findById(createEventRequest.getAdminId()).orElseThrow(() -> new RuntimeException("Admin Not Found"));
        event.setCollege(college);
        event.setAdmin(admin);
        eventRepository.save(event);
        return getEventResponse(event, parsedDate, college);
    }



    @PreAuthorize("hasAnyRole('Admin','Student')")
    public EventResponse findEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event Not Found"));
        return getEventResponse(event,event.getDate(),event.getCollege());
    }
    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findByCollege(Long collegeId,Long studentId) {
        // .getOne() is a lazy loading method and this is called only when you try
        // access it using getters or setters
        // If not found it will throw javax.persistence.EntityNotFoundException Exception
        // Best when you know that the entity exists for sure

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = eventRepository.findByCollege(collegeRepository.getOne(collegeId));
        return filterEvents(events,student);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findByLocation(String location,Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = eventRepository.findByLocation(location);
        return filterEvents(events,student);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findByDate(String date,Long studentId) {
        LocalDate parsedDate = LocalDate.parse(date);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = eventRepository.findByDate(parsedDate);
        return filterEvents(events,student);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findByName(String name,Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = eventRepository.findByName(name);
        return filterEvents(events,student);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findByEventType(EventType eventType,Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = eventRepository.findByEventType(eventType);
        return filterEvents(events,student);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public EventResponse findByCollegeAndName(College college,String name){
        Event event = eventRepository.findByCollegeAndName(college,name).orElseThrow(() -> new RuntimeException("Event Not Found"));
        return getEventResponse(event,event.getDate(),event.getCollege());
    }

    @PreAuthorize("hasRole('Admin')")
    public EventResponse updateEvent(UpdateEventRequest updateEventRequest){
        Event existingEvent = eventRepository.findByCollegeAndName(collegeRepository.getOne(updateEventRequest.getCollegeId()),updateEventRequest.getName()).get();
        if(updateEventRequest.getName() != null){
            existingEvent.setName(updateEventRequest.getName());
        }
        if(updateEventRequest.getLocation() != null){
            existingEvent.setLocation(updateEventRequest.getLocation());
        }
        if(updateEventRequest.getMaxRegistrationLimit() != null){
            existingEvent.setMaxRegistrationLimit(updateEventRequest.getMaxRegistrationLimit());
        }
        if(updateEventRequest.getType() != null){
            existingEvent.setType(updateEventRequest.getType());
        }
        if(updateEventRequest.getEventType() != null){
            existingEvent.setEventType(updateEventRequest.getEventType());
        }
        if(updateEventRequest.getDate() != null){
            existingEvent.setDate(LocalDate.parse(updateEventRequest.getDate()));
        }
        eventRepository.save(existingEvent);
        return getEventResponse(existingEvent,existingEvent.getDate(),existingEvent.getCollege());
    }

    @PreAuthorize("hasRole('Admin')")
    public String deleteEventById(Long id){
        eventRepository.deleteById(id);
        return "Event with id: "+ id + "Deleted";
    }

    @Transactional
    @PreAuthorize("hasRole('Admin')")
    public String deleteByCollegeAndName(Long collegeId,String name){
        College college = collegeRepository.findById(collegeId).get();
        eventRepository.deleteByCollegeAndName(college,name);
        return "Event with name: "+ name + " Deleted";
    }

    @Transactional
    @PreAuthorize("hasAnyRole('Admin','Student')")
    public Set<EventResponse> findEvents(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Set<Event> events = new HashSet<>();
        Set<EventResponse> eventResponses = new HashSet<>();
        if(user.getRole() == Role.Student){
            Student student = user.getStudent();
            events.addAll(eventRepository.findByCollege(student.getCollege()));
            events.addAll(eventRepository.findByType(Type.Inter_Event));
        }else{
            Admin admin = adminRepository.getOne(userId);
            events.addAll(eventRepository.findByCollege(admin.getCollege()));
        }
        for(Event event : events){
            eventResponses.add(getEventResponse(event,event.getDate(),event.getCollege()));
        }
        return eventResponses;
    }
    public void sendReminders(){
        LocalDate tomorrow  = LocalDate.now().plusDays(1);
        Set<Event> tomorrowEvents = eventRepository.findByDate(tomorrow);
        System.out.println("TOM EVENTS:"+tomorrowEvents.size());
        for(Event event : tomorrowEvents){
            System.out.println("Event id:" + event.getId());
            List<Student> students = event.getRegistrations().stream().map(Registration::getStudent).collect(Collectors.toList());
            for(Student student : students){
                System.out.println("Student id:" + student.getId());
                emailService.sendReminderEmail(student,event);
            }
        }

    }
}
