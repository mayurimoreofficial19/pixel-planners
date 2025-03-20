package com.eventvista.event_vista.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Invalid username. Must be between 3 and 20 characters.")
    private String username;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Invalid username. Must be between 3 and 20 characters.")
    private String newPassword;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Invalid username. Must be between 3 and 20 characters.")
    private String verifyPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
