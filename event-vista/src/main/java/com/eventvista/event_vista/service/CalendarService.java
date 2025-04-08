package com.eventvista.event_vista.service;


import com.eventvista.event_vista.data.CalendarRepository;
import com.eventvista.event_vista.model.Calendar;
import com.eventvista.event_vista.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public Optional<Calendar> findCalendarByUser(User user) {
        return calendarRepository.findByUser(user);
    }

    public Optional<Calendar> findCalendarById(Integer id, User user) {
        return calendarRepository.findByIdAndUser(id, user);
    }

    public Calendar addCalendar(Calendar calendar, User user) {
        Optional<Calendar> existingCalendar = calendarRepository.findByUser(user);
        if (existingCalendar.isPresent()) {
            throw new RuntimeException("User already has a calendar");
        }
        calendar.setUser(user);
        return calendarRepository.save(calendar);
    }

    public boolean deleteCalendar(Integer id, User user) {
        Optional<Calendar> calendar = calendarRepository.findByIdAndUser(id, user);
        if (calendar.isPresent()) {
            calendarRepository.delete(calendar.get());
            return true;
        }
        return false;
    }

}



