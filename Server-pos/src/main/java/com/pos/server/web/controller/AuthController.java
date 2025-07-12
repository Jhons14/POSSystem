package com.pos.server.web.controller;


import com.pos.server.domain.dto.AuthenticationRequest;
import com.pos.server.domain.dto.AuthenticationResponse;
import com.pos.server.domain.dto.ErrorResponse;
import com.pos.server.domain.service.PosUserDetailsService;
import com.pos.server.web.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PosUserDetailsService posUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/authenticate")
    public ResponseEntity<?> createToken (@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = posUserDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>(new ErrorResponse("Credenciales incorrectas"), HttpStatus.FORBIDDEN);
        }
    }
}
