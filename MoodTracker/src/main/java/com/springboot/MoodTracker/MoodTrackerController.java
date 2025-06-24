package com.springboot.MoodTracker;

import com.springboot.MoodTracker.DTO.UserDTO;
import com.springboot.MoodTracker.DTO.UserLogInDTO;
import com.springboot.MoodTracker.Reopsitory.UserRepository;
import com.springboot.MoodTracker.Service.UserService;
import com.springboot.MoodTracker.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoodTrackerController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public MoodTrackerController(JwtUtil jwtUtil, UserService userService, UserRepository userRepository, PasswordEncoder encoder, AuthenticationManager authManager){
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authManager;
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> userSignUp(@RequestBody UserDTO userDTO){
        userService.createNewUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Welcome to MoodTracker " + userDTO.getName());
    }

    @PostMapping("/auth/log-in")
    public ResponseEntity<String> userLogIn(@RequestBody UserLogInDTO logInDTO){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInDTO.getUserEmail(), logInDTO.getPassword())
        );
        if(auth.isAuthenticated()){
            String token = jwtUtil.generateToken(logInDTO.getUserEmail());
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60*60)//one hour
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Welcome back!");
        }else{
            throw new BadCredentialsException("Incorrect email or password");
        }
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException e){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}
