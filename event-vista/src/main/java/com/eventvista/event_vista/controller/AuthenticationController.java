package com.eventvista.event_vista.controller;

import com.eventvista.event_vista.data.UserRepository;
import com.eventvista.event_vista.model.User;
import com.eventvista.event_vista.model.dto.LoginFormDTO;
import com.eventvista.event_vista.model.dto.RegisterFormDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.CrossOrigin;Object;

import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    //    View all Users
    @GetMapping("/all")
    public List<User> getAllUsers(){

        return (List<User>) userRepository.findAll();
    }

    //    Registration From authentication chapter unit 2, with JSON RequestBody
    @PostMapping("/register")
    public ResponseEntity<?> processRegistrationForm(@RequestBody @Valid RegisterFormDTO registerFormDTO,
                                                     Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        String username = registerFormDTO.getUsername();
        String emailAddress = registerFormDTO.getEmailAddress();
        String verifyEmailAddress = registerFormDTO.getVerifyEmailAddress();
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();

        User existingUsername = userRepository.findByUsername(username);
        User existingEmailAddress = userRepository.findByEmail(emailAddress);

        if (registerFormDTO.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.isEmpty", "Username is required.");
        } else if (existingUsername != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists.");
        }

        if (registerFormDTO.getEmailAddress().isEmpty()) {
            errors.rejectValue("email", "email.isEmpty", "Email is required.");
        } else if (existingEmailAddress != null) {
            errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists.");
        }

        if (registerFormDTO.getVerifyEmailAddress().isEmpty()) {
            errors.rejectValue("verifyEmail", "verifyEmail.isEmpty", "Verify Email is required.");
        } else if (!emailAddress.equals(verifyEmailAddress)) {
            errors.rejectValue("email", "emails.mismatch", "Emails do not match.");
        }

        if (registerFormDTO.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.isEmpty", "Password is required.");
        } else if (registerFormDTO.getVerifyPassword().isEmpty()) {
            errors.rejectValue("verifyPassword", "verifyPassword.isEmpty", "Verify Password is required.");
        } else if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match.");
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        } else {
            User newUser = new User(username, emailAddress, password);
            setUserInSession(request.getSession(), newUser);
            userRepository.save(newUser);

            return ResponseEntity.ok("User was successfully created!");
        }
    }

    //    Registration
    @PostMapping("/login")
    public ResponseEntity<?> processLoginForm(@RequestBody @Valid LoginFormDTO loginFormDTO,
                                              Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        String emailAddress = loginFormDTO.getEmailAddress();

        User currentUsername = userRepository.findByUsername(username);
        User currentEmailAddress = userRepository.findByEmail(emailAddress);

        if (username.contains("@") && username.contains(".com")) {
            errors.rejectValue("username", "username.invalid", "The username is not your email.");
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        } else if (currentUsername == null) {
            errors.rejectValue("username", "username.invalid", "The given username does not exist.");
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        if (currentEmailAddress == null) {
            errors.rejectValue("email", "email.invalid", "The given email does not exist.");
        }

        if (!currentUsername.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        } else {
            setUserInSession(request.getSession(), currentUsername);

            return ResponseEntity.ok("User logged in successfully!");
        }
    }

    //    Get current user from the session to use for page authorization, returns HTTP response with Ok status and the user from session
//    ResponseEntity which encapsulates the entire HTTP response, encompassing the status code, headers, and body.
    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in!");
        }
//        System.out.println("User not found");
        return ResponseEntity.ok(user);
    }

    //    Find user by username
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        return ResponseEntity.ok(user);
    }


    //    Logout
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        request.getSession().invalidate();
        return ResponseEntity.ok("User is logged out!");
    }


    //    Delete User
    @PostMapping("/delete")
    public void deleteUser(@RequestParam Integer userId){
        userRepository.deleteById(userId);
    }

    //    Edit User method
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid RegisterFormDTO registerFormDTO,
                                        Errors errors, HttpSession session) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        // Get User from session
        User userToUpdate = getUserFromSession(session);

        // Updates User data only for those that were changed
        String username = registerFormDTO.getUsername();
        String emailAddress = registerFormDTO.getEmailAddress();
        String verifyEmailAddress = registerFormDTO.getVerifyEmailAddress();
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        //Integer profileImage = registerFormDTO.getProfileImage();

        User existingUsername = userRepository.findByUsername(username);
        User existingEmailAddress = userRepository.findByEmail(emailAddress);

        String currentUsername = userToUpdate.getUsername();
        String currentEmailAddress = userToUpdate.getEmail();

        // Collect errors
        if (!currentUsername.equalsIgnoreCase(username)) {
            if (registerFormDTO.getUsername().isEmpty()) {
                errors.rejectValue("username", "username.isEmpty", "Username is required.");
            } else if (existingUsername != null) {
                errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists.");
            }
        }

        if (!currentEmailAddress.equalsIgnoreCase(emailAddress)) {
            if (registerFormDTO.getEmailAddress().isEmpty()) {
                errors.rejectValue("email", "email.isEmpty", "Email is required.");
            } else if (existingEmailAddress != null) {
                errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists.");
            }
        }

        if (registerFormDTO.getVerifyEmailAddress().isEmpty()) {
            errors.rejectValue("verifyEmail", "verifyEmail.isEmpty", "Verify Email is required.");
        } else if (!emailAddress.equals(verifyEmailAddress)) {
            errors.rejectValue("email", "emails.mismatch", "Emails do not match.");
        }

        if (registerFormDTO.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.isEmpty", "Password is required.");
        } else if (registerFormDTO.getVerifyPassword().isEmpty()) {
            errors.rejectValue("verifyPassword", "verifyPassword.isEmpty", "Verify Password is required.");
        } else if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match.");
        }

        // If no errors, save updated User to database
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        } else {
            userToUpdate.setUsername(username);
            userToUpdate.setEmail(emailAddress);
            final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userToUpdate.setPwHash(encoder.encode(password));
            //userToUpdate.setProfileImage(profileImage);

            userRepository.save(userToUpdate);
            return ResponseEntity.ok("User successfully updated!");
        }
    }
}
