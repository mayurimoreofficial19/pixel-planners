package com.eventvista.event_vista.model.dto;

public class RegisterFormDTO extends LoginFormDTO {


    private String verifyPassword;

    private String verifyEmailAddress;

    //private Integer profileImage;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public String getVerifyEmailAddress() {
        return verifyEmailAddress;
    }

    public void setVerifyEmailAddress(String verifyEmailAddress) {
        this.verifyEmailAddress = verifyEmailAddress;
    }

//    public Integer getProfileImage() {
//        return profileImage;
//    }
//
//    public void setProfileImage(Integer profileImage) {
//        this.profileImage = profileImage;
//    }
}