package com.exam.controller;


import com.exam.config.JwtUtils;
import com.exam.helper.UserNotFoundException;
import com.exam.model.User;
import com.exam.serviceImpl.UserDetailsServiceImpl;
import com.exam.jwt.JwtRequest;
import com.exam.jwt.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    //generate token
    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            System.out.println("User not found.");
            throw new Exception("User not found");
        }

        //authenticate
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException ex) {
            throw new Exception("USER DISABLED" + ex.getMessage());
        } catch (BadCredentialsException ex) {
            throw new Exception("Invalid Credentials" + ex.getMessage());
        }
    }

    //return the detail of current user
    @GetMapping("/currentUser")
    public User getCurrentUser(Principal principal) {
        return ((User)this.userDetailsService.loadUserByUsername(principal.getName()));
    }

}
