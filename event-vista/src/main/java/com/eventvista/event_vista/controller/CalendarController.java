package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Calendar;
import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.data.CalendarRepository;
import com.eventvista.event_vista.data.EventRepository;
import com.eventvista.event_vista.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin(origins = "http://localhost:3000")
public class CalendarController {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Iterable<Calendar>> getAllCalendars() {
        return ResponseEntity.ok(calendarRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarById(@PathVariable Integer id) {
        return calendarRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Calendar> createCalendar(@RequestBody Calendar calendar) {
        Calendar savedCalendar = calendarRepository.save(calendar);
        return ResponseEntity.ok(savedCalendar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calendar> updateCalendar(@PathVariable Integer id, @RequestBody Calendar calendar) {
        if (!calendarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return calendarRepository.findById(id)
                .map(existingCalendar -> {
                    existingCalendar.setName(calendar.getName());
                    existingCalendar.setUser(calendar.getUser());
                    existingCalendar.setEvents(calendar.getEvents());
                    existingCalendar.setTimezone(calendar.getTimezone());
                    existingCalendar.setGoogleCalendarId(calendar.getGoogleCalendarId());
                    existingCalendar.setLastSyncTime(calendar.getLastSyncTime());
                    existingCalendar.setSyncEnabled(calendar.isSyncEnabled());
                    return ResponseEntity.ok(calendarRepository.save(existingCalendar));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Integer id) {
        if (!calendarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        calendarRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Get calendar for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<Calendar> getCalendarByUser(@PathVariable int userId) {
        return userRepository.findById(userId)
                .map(user -> calendarRepository.findByUser(user)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all events for a calendar
    @GetMapping("/{calendarId}/events")
    public ResponseEntity<List<Event>> getCalendarEvents(@PathVariable int calendarId) {
        return calendarRepository.findById(calendarId)
                .map(calendar -> ResponseEntity.ok(calendar.getEvents()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new calendar for a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Calendar> createCalendar(@PathVariable int userId, @RequestBody Calendar calendar) {
        return userRepository.findById(userId)
                .map(user -> {
                    calendar.setUser(user);
                    return ResponseEntity.ok(calendarRepository.save(calendar));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Update calendar settings
    @PutMapping("/{calendarId}")
    public ResponseEntity<Calendar> updateCalendar(@PathVariable int calendarId, @RequestBody Calendar updatedCalendar) {
        return calendarRepository.findById(calendarId)
                .map(calendar -> {
                    calendar.setName(updatedCalendar.getName());
                    calendar.setTimezone(updatedCalendar.getTimezone());
                    calendar.setGoogleCalendarId(updatedCalendar.getGoogleCalendarId());
                    calendar.setSyncEnabled(updatedCalendar.isSyncEnabled());
                    if (updatedCalendar.isSyncEnabled()) {
                        calendar.setLastSyncTime(LocalDateTime.now());
                    }
                    return ResponseEntity.ok(calendarRepository.save(calendar));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get events for a specific date range
    @GetMapping("/{calendarId}/events/range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @PathVariable int calendarId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return calendarRepository.findById(calendarId)
                .map(calendar -> {
                    List<Event> eventsInRange = calendar.getEvents().stream()
                            .filter(event -> {
                                LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getTime());
                                return !eventDateTime.isBefore(start) && !eventDateTime.isAfter(end);
                            })
                            .toList();
                    return ResponseEntity.ok(eventsInRange);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

