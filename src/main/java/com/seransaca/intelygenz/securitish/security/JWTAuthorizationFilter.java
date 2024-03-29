package com.seransaca.intelygenz.securitish.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seransaca.intelygenz.securitish.web.dto.error.ApiError;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private static String SECRET = "Safe-Ish$2022";

    private static Claims claims;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
        try {
            if (checkJWTToken(request, response)) {
                claims = validateToken(request);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }else {
                SecurityContextHolder.clearContext();
            }
        } catch (ExpiredJwtException e) {
            ApiError error = createError("JWT expired", e);
            sendResponse(response, error);
            return;
        } catch (MalformedJwtException e) {
            ApiError error = createError("JWT malformed", e);
            sendResponse(response, error);
            return;
        } catch (UnsupportedJwtException e) {
            ApiError error = createError("JWT unsupported", e);
            sendResponse(response, error);
            return;
        }
        chain.doFilter(request, response);
    }

    private static ApiError createError(String msg, Exception ex){
        return new ApiError(HttpStatus.UNAUTHORIZED, msg, ex);
    }


    private static void sendResponse(HttpServletResponse response, ApiError error) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        response.setStatus(error.getHttpStatus().value());
        response.setContentType("application/json");
        mapper.findAndRegisterModules();
        out.print(mapper.writeValueAsString(error));
        out.flush();
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    protected static String getSafeboxName(){
        return claims.getId();
    }

    protected static String getSafeboxPass(){
        return claims.getSubject();
    }

}
