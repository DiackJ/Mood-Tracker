package com.springboot.MoodTracker.Util;

import com.springboot.MoodTracker.Reopsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.springboot.MoodTracker.Model.User;
import org.springframework.stereotype.Service;

//service class responsible for generating the user's details and returning a valid user upon log-in
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found"));
        return new CustomUserDetails(user);
    }
}
