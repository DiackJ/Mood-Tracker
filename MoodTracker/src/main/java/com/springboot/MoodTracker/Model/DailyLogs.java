package com.springboot.MoodTracker.Model;

import com.springboot.MoodTracker.Enum.Mood;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class DailyLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;
    private Mood mood;
    private double sleepHours;
    private String reflectionBlurb;
    private Timestamp createdAt;

    public DailyLogs(){}

    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}
    public User getUserId(){return this.userId;}
    public void setUserId(User userId){this.userId = userId;}
    public Mood getMood(){return this.mood;}
    public void setMood(Mood mood){this.mood = mood;}
    public double getSleepHours(){return this.sleepHours;}
    public void setSleepHours(double sleepHours){this.sleepHours = sleepHours;}
    public String getReflectionBlurb(){return this.reflectionBlurb;}
    public void setReflectionBlurb(String blurb){this.reflectionBlurb = blurb;}
    public Timestamp getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(Timestamp createdAt){this.createdAt = createdAt;}
}
