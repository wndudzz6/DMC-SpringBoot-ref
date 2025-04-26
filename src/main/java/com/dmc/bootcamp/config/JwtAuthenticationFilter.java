package com.dmc.bootcamp.config;

import com.dmc.bootcamp.repository.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final TokenBlacklistRepository tokenBlacklistRepository;
    @Value("${security.jwt.secret-key}")
    private String jwtsecretKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && !tokenBlacklistRepository.contains(token)) {
            try {
                Jwt jwt = decodeToken(token); // 토큰 디코딩
                String userId = jwt.getSubject(); // userId 가져오기

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getClaim("role")));
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                logger.info("Authentication created: {}", auth.isAuthenticated()); // 추가
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                logger.error("Invalid JWT token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Jwt decodeToken(String token) {
        var secretKey = new SecretKeySpec(jwtsecretKey.getBytes(), "HmacSHA256");
        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
        return decoder.decode(token);
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
