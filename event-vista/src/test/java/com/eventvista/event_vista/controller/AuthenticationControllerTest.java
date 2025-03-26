package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.dto.LoginFormDTO;
import com.eventvista.event_vista.model.dto.RegisterFormDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Errors errors;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testProcessRegistrationForm() {
        RegisterFormDTO registerFormDTO = new RegisterFormDTO();
        registerFormDTO.setUsername("testuser");
        registerFormDTO.setEmailAddress("test@example.com");
        registerFormDTO.setVerifyEmailAddress("test@example.com");
        registerFormDTO.setPassword("password");
        registerFormDTO.setVerifyPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.findByEmailAddress("test@example.com")).thenReturn(null);

        ResponseEntity<?> response = authenticationController.processRegistrationForm(registerFormDTO, errors, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testProcessLoginForm() {
        LoginFormDTO loginFormDTO = new LoginFormDTO();
        loginFormDTO.setUsername("testuser");
        loginFormDTO.setPassword("password");
        loginFormDTO.setEmailAddress("test@example.com");

        User user = new User();
        user.setUsername("testuser");
        user.setPwHash(new BCryptPasswordEncoder().encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(userRepository.findByEmailAddress("test@example.com")).thenReturn(user);

        ResponseEntity<?> response = authenticationController.processLoginForm(loginFormDTO, errors, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(session, times(1)).setAttribute("user", user.getId());
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setId(1);
        when(session.getAttribute("user")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authenticationController.getCurrentUser(session);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testLogout() {
        ResponseEntity<?> response = authenticationController.logout(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(session, times(1)).invalidate();
    }
}