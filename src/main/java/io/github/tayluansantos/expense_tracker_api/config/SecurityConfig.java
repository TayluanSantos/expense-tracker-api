package io.github.tayluansantos.expense_tracker_api.config;

import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import io.github.tayluansantos.expense_tracker_api.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.
                csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/users/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST,"/users/**").permitAll();
                    auth.requestMatchers("/expenses/**").hasAnyRole("USER","ADMIN");
                    auth.requestMatchers(HttpMethod.GET,"/categories").permitAll();
                    auth.requestMatchers("/categories/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService (UserRepository userRepository){
        return new CustomUserDetailsService(userRepository);
    }
}
