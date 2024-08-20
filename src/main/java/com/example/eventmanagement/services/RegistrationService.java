package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.Registration;
import com.example.eventmanagement.models.Student;
import com.example.eventmanagement.repositories.EventRepository;
import com.example.eventmanagement.repositories.RegistrationRepository;
import com.example.eventmanagement.repositories.StudentRepository;
import com.example.eventmanagement.response.RegistrationResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class RegistrationService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private EmailService emailService;

    public RegistrationService(StudentRepository studentRepository, EventRepository eventRepository, RegistrationRepository registrationRepository, EventService eventService) {
        this.studentRepository = studentRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.eventService = eventService;
    }

    @NotNull
    public static RegistrationResponse getRegistrationResponse(Student student, Event event) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setEmail(student.getUser().getEmail());
        registrationResponse.setEventDate(event.getDate());
        registrationResponse.setEventCollege(event.getCollege().getName());
        registrationResponse.setEventType(event.getEventType());
        registrationResponse.setType(event.getType());
        registrationResponse.setEventLocation(event.getLocation());
        registrationResponse.setStudentCollege(student.getCollege().getName());
        registrationResponse.setEventName(event.getName());
        registrationResponse.setStudentName(student.getUser().getName());
        return registrationResponse;
    }

    @Transactional
    @PreAuthorize("hasRole('Student')")
    public RegistrationResponse registerEvent(Long studentId, Long eventId){
        Registration registration = new Registration();
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student Not Found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Student Not Found"));

        // No need to put this logic as we will be showing only the events that a student can participate in the frontend
//        Set<Event> eligibleEvents = eventService.findEvents(studentId);
//        if(!eligibleEvents.contains(event)){
//            throw new RuntimeException("You are not eligible to attend the event");
//        }

        if(Objects.equals(event.getMaxRegistrationLimit(), event.getCurrentRegistrations())){
            throw new RuntimeException("Maximum Registration Limit Exceeded");
        }

        List<Registration> registrationList = event.getRegistrations();
        for(Registration reg : registrationList){
            if(reg.getStudent().equals(student)){
                throw new RuntimeException("Student Already Registered");
            }
        }
        event.setCurrentRegistrations(event.getCurrentRegistrations() + 1);
        registration.setStudent(student);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDate.now());


        List<Registration> currentStudentRegistrations = student.getRegistrations();
        currentStudentRegistrations.add(registration);
        student.setRegistrations(currentStudentRegistrations);

        List<Registration> currentEventRegistrations = event.getRegistrations();
        currentEventRegistrations.add(registration);
        event.setRegistrations(currentEventRegistrations);
        registrationRepository.save(registration);
        emailService.sendAcknowledgementEmail(student,event);
        return getRegistrationResponse(student, event);
    }

    @Transactional
    @PreAuthorize("hasRole('Student')")
    public void deleteRegistration(Long studentId, Long eventId){
        Registration registration = registrationRepository.findByStudentAndEvent(studentRepository.getOne(studentId), eventRepository.getOne(eventId));
        System.out.println(registration.getId());
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student Not Found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event Not Found"));
        List<Registration> currentStudentRegistrations = student.getRegistrations();
        List<Registration> currentEventRegistrations = event.getRegistrations();
        currentStudentRegistrations.remove(registration);
        currentEventRegistrations.remove(registration);
        student.setRegistrations(currentStudentRegistrations);
        studentRepository.save(student);
        event.setRegistrations(currentEventRegistrations);
        eventRepository.save(event);
        registrationRepository.deleteById(registration.getId());
    }
}
