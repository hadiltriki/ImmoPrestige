package com.tekup.miniproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
        @Autowired
        private UserDetailsService uds;

        @Autowired
        private BCryptPasswordEncoder encoder;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/","/ads","/login",
                                                                "/images/**")
                                                .permitAll().anyRequest().authenticated())
                                // Tous les endpoints nécessitent une
                                // authentification

                                // .formLogin(Customizer.withDefaults()); // Active le formulaire de login par
                                // défaut
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .permitAll()
                                               .defaultSuccessUrl("/home", true) // Rediriger vers home après login
                                                /*/.successHandler((request, response, authentication) -> {
                                                        // Si une URL sauvegardée existe (SavedRequest), y rediriger,
                                                        // sinon rediriger vers home "/"
                                                        var savedRequest = (org.springframework.security.web.savedrequest.DefaultSavedRequest) request
                                                                        .getSession()
                                                                        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                                                        if (savedRequest != null) {
                                                                response.sendRedirect(savedRequest.getRequestURL());
                                                        } else {
                                                                response.sendRedirect("/home"); // URL par défaut si
                                                                                            // aucune URL
                                                                                            // sauvegardée n'existe
                                                        }
                                                })*/)
                                .exceptionHandling((exceptionHandling) -> exceptionHandling
                                                .accessDeniedPage("/access-denied"))
                                .logout((logout) -> logout
                                .logoutUrl("/logout") // URL pour la déconnexion
                                .logoutSuccessUrl("/") // URL après la déconnexion réussie
                                .permitAll()
                            );

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.withUsername("user")
                                // password
                                .password("{bcrypt}$2y$10$eAO3nmIvfYPu618cI5NodeDOi/shGacf75FuS3Ue08tqs4keSK9cK")
                                .roles("USER").build();
                return new InMemoryUserDetailsManager(user);

        }
        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(uds);
                authenticationProvider.setPasswordEncoder(encoder);
                return authenticationProvider;
        }

}
