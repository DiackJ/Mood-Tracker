package com.springboot.MoodTracker.Service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.MoodTracker.DTO.UserDTO;
import com.springboot.MoodTracker.Enum.Gender;
import com.springboot.MoodTracker.Model.User;
import com.springboot.MoodTracker.Reopsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder){
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    //create a new user
    public void createNewUser(UserDTO dto){
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setAge(dto.getAge());

        try{
            user.setGender(Gender.valueOf(dto.getGender()));
        }catch(IllegalArgumentException e){
            throw new RuntimeException("Invalid input");
        }
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }

}
