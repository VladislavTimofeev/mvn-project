package com.vlad.config;

import com.vlad.security.jwt.JwtAuthenticationFilter;
import com.vlad.security.jwt.JwtProperties;
import com.vlad.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.vlad.entity.Role.*;


@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserServiceImpl userServiceImpl;
    private static final String UNAUTHORIZED_RESPONSE = """
            {
                "error": "Unauthorized",
                "message": "Authentication required"
            }
            """;
    private static final String FORBIDDEN_RESPONSE = """
            {
                "error": "Forbidden",
                "message": "Access denied"
            }
            """;
    private static final String LOGIN = "/login";

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v2/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/**")
                        .hasAuthority(ADMIN.getAuthority())

                        .requestMatchers(HttpMethod.POST, "/api/v2/drivers/**")
                        .hasAuthority(CARRIER.getAuthority())

                        .requestMatchers(HttpMethod.POST, "/api/v2/vehicles/**")
                        .hasAuthority(CARRIER.getAuthority())

                        .requestMatchers(HttpMethod.POST, "/api/v2/trips/**")
                        .hasAuthority(CARRIER.getAuthority())

                        .requestMatchers(HttpMethod.POST, "/api/v2/requests/**")
                        .hasAuthority(CUSTOMER.getAuthority())

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req,
                                                   resp,
                                                   authException) -> {
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            resp.setContentType("application/json");
                            resp.getWriter().write(UNAUTHORIZED_RESPONSE);
                        })
                        .accessDeniedHandler((req,
                                              resp,
                                              accessDeniedException) -> {
                            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            resp.setContentType("application/json");
                            resp.getWriter().write(FORBIDDEN_RESPONSE);
                        })
                )
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurityfilterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(LOGIN, "/welcome", "/users/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/drivers/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("/vehicles/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("/trips/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("/requests/create").hasAuthority(CUSTOMER.getAuthority())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(LOGIN))
                .formLogin(login -> login
                        .loginPage(LOGIN)
                        .defaultSuccessUrl("/welcome", true)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
