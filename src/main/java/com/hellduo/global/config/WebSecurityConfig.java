package com.hellduo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshTokenRepository;
import com.hellduo.global.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

        http.csrf((csrf) -> csrf.disable()); // CSRF 비활성화

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안함
        );

        // URL별 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/api/v1/users/signup", "/api/v1/users/trainerSignup", "/api/v1/users/login","/api/v1/page/**","/js/**")
                        .permitAll() // signup, trainerSignup, login은 인증 없이 접근 가능
                        .requestMatchers("/api/v1/users/logout","/api/v1/users/withdrawal", "/api/v1/users", "/api/v1/users/trainer",
                                "/api/v1/users/update","/api/v1/userImage/**","/api/v1/board/**","/api/v1/comment","/api/v1/pt/**","/api/v1/users/role")
                        .authenticated() //인증된 사용자만 접근 가능
                        .anyRequest().denyAll()  // 나머지 요청은 모두 거부
        );

        // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

