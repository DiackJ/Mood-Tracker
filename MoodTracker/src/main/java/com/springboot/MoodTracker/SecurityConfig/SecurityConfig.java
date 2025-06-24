package com.springboot.MoodTracker.SecurityConfig;

import com.springboot.MoodTracker.Util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService){
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService =userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(out -> out
                        .logoutUrl("/log-out")
                        .deleteCookies("jwt")
                        .logoutSuccessUrl("/auth/log-in?log-out")
                )
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //password encryption
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    //looks up user and validates password
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        //note to self: look into newer preferred way to execute the following line since depreciation
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    //needed for stateless authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
