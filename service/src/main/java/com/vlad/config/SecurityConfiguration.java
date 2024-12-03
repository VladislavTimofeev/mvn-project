package com.vlad.config;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.entity.Role;
import com.vlad.service.UserService;
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

import static com.vlad.entity.Role.ADMIN;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .requestMatchers("/", "/login", "/login/**", "/users/registration", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
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
                userDetails = userService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                        email,
                        "oauth2_generated",
                        oidcUser.getName() != null ? oidcUser.getName() : "Unknown name",
                        oidcUser.getPhoneNumber(),
                        oidcUser.getAddress().toString(),
                        Role.GUEST
                );
                userService.save(userCreateEditDto);
                userDetails = userService.loadUserByUsername(email);
            }
            return new DefaultOidcUser(
                    userDetails.getAuthorities(),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        };
    }
}
