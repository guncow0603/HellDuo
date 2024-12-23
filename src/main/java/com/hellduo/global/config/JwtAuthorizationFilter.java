package com.hellduo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellduo.domain.user.dto.response.UserLoginRes;
import com.hellduo.domain.user.entity.User;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshToken;
import com.hellduo.global.redis.RefreshTokenRepository;
import com.hellduo.global.security.UserDetailsImpl;
import com.hellduo.global.security.UserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.getTokenFromRequest(request, JwtUtil.ACCESS_TOKEN_HEADER);
        if (Objects.nonNull(token)) {
            if (jwtUtil.validateToken(token)) {
                setContext(token);
            } else {
                String refreshtoken = jwtUtil.getTokenFromRequest(request,
                        JwtUtil.REFRESH_TOKEN_HEADER);

                if (jwtUtil.validateToken(refreshtoken)) {

                    refreshtoken = JwtUtil.BEARER_PREFIX + refreshtoken;

                    RefreshToken rf = refreshTokenRepository.findRefreshTokenByIdWithThrow(
                            refreshtoken);

                    Long userId = rf.getUserId();
                    UserDetailsImpl userDetails = userDetailsService.loadUserById(userId);

                    User user = userDetails.getUser();

                    String accessToken = jwtUtil.createAccessToken(user.getEmail(),
                            user.getRole());

                    jwtUtil.addAccessJwtToCookie(accessToken, response);
                    accessToken = accessToken.substring(7);
                    setContext(accessToken);

                } else {
                    UserLoginRes res = new UserLoginRes(
                            "유효하지 않은 토큰입니다.");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(res));
                    return;

                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setContext(String token) {
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // email -> search user
        String email = info.getSubject();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // -> put this in userDetails
        UserDetailsImpl userDetails = userDetailsService.getUserDetails(email);
        // ->  put this in authentication principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        // -> put this in securityContent
        context.setAuthentication(authentication);
        // -> put this in SecurityContextHolder
        SecurityContextHolder.setContext(context);
    }

}