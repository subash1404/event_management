package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.Feedback;
import com.example.eventmanagement.models.Registration;
import com.example.eventmanagement.models.Student;
import com.example.eventmanagement.repositories.EventRepository;
import com.example.eventmanagement.repositories.FeedbackRepository;
import com.example.eventmanagement.repositories.RegistrationRepository;
import com.example.eventmanagement.repositories.StudentRepository;
import com.example.eventmanagement.request.CreateFeedbackRequest;
import com.example.eventmanagement.response.FeedbackResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.FeatureDescriptor;
import java.util.List;

@Service
public class FeedbackService {
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    public FeedbackService(StudentRepository studentRepository, EventRepository eventRepository,FeedbackRepository feedbackRepository) {
        this.studentRepository = studentRepository;
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @NotNull
    public static FeedbackResponse getFeedbackResponse(Feedback feedback, Student student, Event event) {
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setMessage(feedback.getMessage());
        feedbackResponse.setEmail(student.getUser().getEmail());
        feedbackResponse.setEventCollege(event.getCollege().getName());
        feedbackResponse.setEventName(event.getName());
        feedbackResponse.setEventDate(event.getDate());
        feedbackResponse.setStudentName(student.getUser().getName());
        feedbackResponse.setStudentCollegeName(student.getCollege().getName());
        return feedbackResponse;
    }


    @Transactional
    @PreAuthorize("hasRole('Student')")
     public FeedbackResponse postFeedback(CreateFeedbackRequest createFeedbackRequest){
        Feedback feedback = new Feedback();
        Student student = studentRepository.getOne(createFeedbackRequest.getStudentId());
        Event event = eventRepository.getOne(createFeedbackRequest.getEventId());

        boolean isRegistered = student.getRegistrations().stream()
                .anyMatch(registration -> registration.getEvent().getId().equals(event.getId()));

        if (!isRegistered) {
            throw new RuntimeException("Student is not registered for the event.");
        }

        feedback.setStudent(student);
        feedback.setEvent(event);
        feedback.setMessage(createFeedbackRequest.getMessage());

        List<Feedback> studentFeedbacks = student.getFeedbacks();
        List<Feedback> eventFeedbacks = event.getFeedbacks();
        studentFeedbacks.add(feedback);
        eventFeedbacks.add(feedback);
        student.setFeedbacks(studentFeedbacks);
        event.setFeedbacks(eventFeedbacks);
        studentRepository.save(student);
        eventRepository.save(event);

        FeedbackResponse feedbackResponse = getFeedbackResponse(feedback, student, event);
        feedbackRepository.save(feedback);

        return feedbackResponse;
    }


    @Transactional
    @PreAuthorize("hasRole('Student')")
    public void deleteFeedback(Long feedbackId){
        Feedback feedback = feedbackRepository.getOne(feedbackId);
        Student student = feedback.getStudent();
        Event event = feedback.getEvent();

        student.getFeedbacks().remove(feedback);
        event.getFeedbacks().remove(feedback);

        studentRepository.save(student);
        eventRepository.save(event);

        feedbackRepository.deleteById(feedbackId);

        System.out.println(student.getFeedbacks());
        System.out.println(event.getFeedbacks());

    }
}
