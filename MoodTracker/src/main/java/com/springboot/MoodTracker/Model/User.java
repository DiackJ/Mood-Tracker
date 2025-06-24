package com.springboot.MoodTracker.Model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.springboot.MoodTracker.Enum.Gender;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Timestamp createdAt;
    @Transient
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<DailyLogs> logs;

    public User(){
        this.logs = new ArrayList<>();
    }

    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}
    public String getEmail(){return this.email;}
    public void setEmail(String email){this.email = email;}
    public String getPassword(){return this.password;}
    public void setPassword(String password){this.password = password;}
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
    public int getAge(){return this.age;}
    public void setAge(int age){this.age = age;}
    public Gender getGender(){return this.gender;}
    public void setGender(Gender gender){this.gender = gender;}
    public Timestamp getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(Timestamp createdAt){this.createdAt = createdAt;}
    public List<DailyLogs> getLogs(){return this.logs;}
    public void setLogs(List<DailyLogs> logs){this.logs = logs;}
}
