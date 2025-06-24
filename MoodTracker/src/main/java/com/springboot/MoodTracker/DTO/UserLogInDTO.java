package com.springboot.MoodTracker.DTO;


public class UserLogInDTO {
    private String userEmail;
    private String password;

    public UserLogInDTO(){}

    public String getUserEmail(){return this.userEmail;}
    public String getPassword(){return this.password;}
}
