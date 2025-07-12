package com.pos.server.web.security;

import com.pos.server.domain.service.PosUserDetailsService;
import com.pos.server.web.security.filter.JwtFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
@Configuration

@EnableWebSecurity
public class SecurityConfig   {

    @Autowired
    private PosUserDetailsService posUserDetailsService;

    @Autowired
    private JwtFilterRequest jwtFilterRequest;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(posUserDetailsService);
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Habilitar CORS y deshabilitar CSRF
                .cors(cors -> cors.configure(http))

                // Configurar rutas permitidas sin autenticación
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**/authenticate",
                                "/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**", "/*/swagger-resources/**",
                                "/*/v2/api-docs", "/webjars/**", "/configuration/security", "/configuration/ui", "/images/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                // Definir la política de manejo de sesiones como "stateless"
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Añadir el filtro JWT personalizado antes del filtro estándar de autenticación
                .addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://front-market-seven.vercel.app")); // o "*"
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // importante si usas cookies o JWT en header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

