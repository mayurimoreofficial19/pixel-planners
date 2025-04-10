package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.model.Calendar;
import com.eventvista.event_vista.model.Event;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.data.CalendarRepository;
import com.eventvista.event_vista.data.EventRepository;
import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.service.CalendarService;
import com.eventvista.event_vista.utilities.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin(origins = "http://localhost:3000")
public class CalendarController {
    private final CalendarService calendarService;
    private final AuthUtil authUtil;

    public CalendarController(CalendarService calendarService, AuthUtil authUtil) {
        this.calendarService = calendarService;
        this.authUtil = authUtil;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyCalendar() {
        User user = authUtil.getUserFromAuthentication();
        return calendarService.findCalendarByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable Integer id) {
        User user = authUtil.getUserFromAuthentication();
        return calendarService.findCalendarById(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCalendar(@RequestBody Calendar calendar) {
        User user = authUtil.getUserFromAuthentication();
        try {
            Calendar savedCalendar = calendarService.addCalendar(calendar, user);
            return ResponseEntity.ok(savedCalendar);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable Integer id) {
        User user = authUtil.getUserFromAuthentication();
        boolean deleted = calendarService.deleteCalendar(id, user);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}




