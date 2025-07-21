package com.pos.server.domain.service;

import com.pos.server.infrastructure.persistence.entity.Cliente;
import com.pos.server.infrastructure.persistence.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PosUserDetailsService implements UserDetailsService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find by username or email\
        Cliente cliente = clienteRepository.findByUsernameOrCorreoElectronico(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Check if user can login
        if (!cliente.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo o bloqueado");
        }
        // Return UserDetails with the hashed password from DB
        UserDetails user = User.builder()
                .username(cliente.getUsername())
                .password("{bcrypt}" + cliente.getPasswordHash()) // ← This is the hashed password from DB
                .authorities("ROLE_USER") // You can customize roles
                .accountExpired(false)
                .accountLocked(cliente.isCuentaBloqueada())
                .credentialsExpired(false)
                .disabled(!cliente.isActivo())
                .build();
        return user;
    }


}
