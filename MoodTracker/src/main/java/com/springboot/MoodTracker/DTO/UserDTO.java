package com.springboot.MoodTracker.DTO;


public class UserDTO {
    private String email;
    private String password;
    private String name;
    private int age;
    private String gender; //enum received as string

    public UserDTO(){}

    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
    public String getName(){return this.name;}
    public int getAge(){return this.age;}
    public String getGender(){return this.gender;}
}
