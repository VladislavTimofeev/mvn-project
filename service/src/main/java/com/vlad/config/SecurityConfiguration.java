package com.vlad.config;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.entity.Role;
import com.vlad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import static com.vlad.entity.Role.CARRIER;
import static com.vlad.entity.Role.CUSTOMER;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserServiceImpl userServiceImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login/**", "/users/registration", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/drivers/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("/vehicles/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("trips/create").hasAuthority(CARRIER.getAuthority())
                        .requestMatchers("/requests/create").hasAuthority(CUSTOMER.getAuthority())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/welcome")
                )
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.oidcUserService())))
                .build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService oidcUserService = new OidcUserService();
        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            String email = (String) oidcUser.getAttributes().get("email");
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("Email not found in OIDC token");
            }
            UserDetails userDetails;
            try {
                userDetails = userServiceImpl.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                        email,
                        "oauth2_generated",
                        oidcUser.getName() != null ? oidcUser.getName() : "Unknown name",
                        oidcUser.getPhoneNumber(),
                        oidcUser.getAddress().toString(),
                        Role.GUEST
                );
                userServiceImpl.save(userCreateEditDto);
                userDetails = userServiceImpl.loadUserByUsername(email);
            }
            return new DefaultOidcUser(
                    userDetails.getAuthorities(),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        };
    }
}
