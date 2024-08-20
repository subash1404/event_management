package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Event;
import com.example.eventmanagement.models.Student;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(Student student, Event event){
        String subject = "Reminder: Upcoming Event Tomorrow - " + event.getName();
        String body = "Dear " + student.getUser().getName() + ",\n\n" +
                "This is a reminder that you have registered for the event " +
                event.getName() + " happening tomorrow.\n" +
                "Event Details:\n" +
                "Date: " + event.getDate() + "\n" +
                "Location: " + event.getLocation() + "\n\n" +
                "We look forward to seeing you there!\n\n" +
                "Best Regards,\nEvent Management Team";

        sendEmail(student.getUser().getEmail(), subject, body);
    }
    public void sendAcknowledgementEmail(Student student, Event event){
        String subject = "Registration successful for the event - " + event.getName();
        String body = "Dear " + student.getUser().getName() + ",\n\n" +
                "You have successfully registered for the event " +
                event.getName() +
                "Event Details:\n" +
                "Date: " + event.getDate() + "\n" +
                "Location: " + event.getLocation() + "\n\n" +
                "We look forward to seeing you there!\n\n" +
                "Best Regards,\nEvent Management Team";

        sendEmail(student.getUser().getEmail(), subject, body);
    }

    private void sendEmail(String toEmail, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("subash.v@superops.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Mail sent successfully to " + toEmail);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
