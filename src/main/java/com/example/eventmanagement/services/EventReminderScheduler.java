package com.example.eventmanagement.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventReminderScheduler {

    private final EventService eventService;

    public EventReminderScheduler(EventService eventService) {
        System.out.println("Inside scheduler");
        this.eventService = eventService;
    }

    @Scheduled(cron = "0 07 9 * * *")
    public void sendEventReminders() {
        eventService.sendReminders();
    }
}
