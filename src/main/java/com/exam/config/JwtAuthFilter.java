package com.exam.config;

import com.exam.controller.AuthController;
import com.exam.jwt.JwtRequest;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.serviceImpl.UserDetailsServiceImpl;
import com.exam.serviceImpl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    Claims claims = null;

    @Autowired
    private UserRole userRole;

    @Autowired
    User user;

    @Autowired
    private JwtRequest jwtRequest;


    public boolean isAdmin() {
        String username = claims.getSubject();
        User user = (User) userDetailsService.loadUserByUsername(username);
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().getRoleId() == 10) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdminAuth() {
        String username = claims.getSubject();
        User user = (User) userDetailsService.loadUserByUsername(username);
        for (GrantedAuthority userRole : user.getAuthorities()) {
            if (userRole.getAuthority().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

    public boolean isTeacherAuth() {
        String username = claims.getSubject();
        User user = (User) userDetailsService.loadUserByUsername(username);
        for (GrantedAuthority userRole : user.getAuthorities()) {
            if (userRole.getAuthority().equals("TEACHER")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        //tu je jeden rozdiel. že AUTHORIZATION je v úvodzovkách.... uvidíme
        System.out.println(authHeader);
        String username;
        String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Invalid token, not start with bearer sting");
            filterChain.doFilter(request, response);
            return;
            //ak nemám authHeader alebo nezačína authHeader s "Bearer", tak nepokračuj a skonči
        } else {
            jwtToken = authHeader.substring(7);
            try {
                username = this.jwtUtils.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                System.out.println("Jwt token has expired.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error");
            }
        }
        //validated

        jwtToken = authHeader.substring(7);
        username = jwtUtils.extractUsername(jwtToken);
        claims = jwtUtils.extractAllClaims(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.jwtUtils.validateToken(jwtToken, userDetails)) {
                //token is valid
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } else {
            System.out.println("Token is not valid");
        }
        filterChain.doFilter(request, response);
    }
}




