package com.eventvista.event_vista.service;

import com.eventvista.event_vista.model.Calendar;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.data.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CalendarService calendarService;

    public UserServiceImpl(UserRepository userRepository, CalendarService calendarService) {
        this.userRepository = userRepository;
        this.calendarService = calendarService;
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public User save(User user) {
        // Save the user first
        User savedUser = userRepository.save(user);

        // Create and save a calendar for the user if they don't have one
        if (savedUser.getCalendar() == null) {
            Calendar calendar = new Calendar();
            calendar.setUser(savedUser);
            calendarService.addCalendar(calendar, savedUser);
        }

        return savedUser;
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return userRepository.existsByEmailAddress(emailAddress);
    }
}

