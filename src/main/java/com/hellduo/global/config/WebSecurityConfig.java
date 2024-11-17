package com.hellduo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellduo.domain.user.dto.response.UserLogoutRes;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshTokenRepository;
import com.hellduo.global.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // authorize
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper,
                refreshTokenRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll()  // 모든 요청을 인증 없이 허용
        );

        http.logout(logout -> logout
                .logoutUrl("/api/v1/users/logout")
                .logoutSuccessUrl("/api/v1")  // Add a leading slash here
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Clear privileges upon successful logout
                    SecurityContextHolder.clearContext();

                    // Additional logout processing logic can be added here
                    log.info("Logout successful. Redirecting to /api/v1");
                    // send response
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter()
                            .write(objectMapper.writeValueAsString(new UserLogoutRes("Logout complete")));
                })
                .deleteCookies("AccessToken", "RefreshToken"));


        // filter
        http.addFilterBefore(jwtAuthorizationFilter(),
                UsernamePasswordAuthenticationFilter.class); // username~ 전에 jwtAuthor 먼저

        return http.build();
    }

}

