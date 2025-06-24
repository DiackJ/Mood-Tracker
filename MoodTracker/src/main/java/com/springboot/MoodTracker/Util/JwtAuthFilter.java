package com.springboot.MoodTracker.Util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//class to define custom authentication logic
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil){
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterchain) throws ServletException, IOException{
        //skip public paths to avoid unnecessary authentication
        String path = req.getServletPath();
        if(path.startsWith("/auth/**")){
            filterchain.doFilter(req, res);
            return;
        }

        //for all protected endpoints, validate jwt token and cookies
        String jwtToken = null;
        String userEmail = null;

        if(req.getCookies() != null){
            for(Cookie cookie : req.getCookies()){
                if(cookie.getName().equals("jwt")){
                    jwtToken = cookie.getValue();
                    userEmail = jwtUtil.extractSubject(jwtToken);
                }
            }
        }

        //check and update the security context holder to make sure the user isn't already logged in
        //if the user email is not null and the user doesn't exist in the security context then:
        // load user, validate token, check credentials, add user to context (aka log them in)
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if(jwtUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterchain.doFilter(req, res);
        }
    }
}
