package com.gymondo.security;

import com.gymondo.errorhandling.ExceptionResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${gymondo.api.key}")
    private String authorizedApiKey;

    private final ExceptionResolver exceptionResolver;

    public AuthenticationFilter(ExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    private static final List<String> exceptionalPaths = Arrays.asList("/api-docs", "/swagger", "/actuator");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!isExceptional(request.getRequestURI())) {
                String apiKey = request.getHeader("API-KEY");

                if (apiKey == null || !apiKey.equals(authorizedApiKey)) {
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid access! API-KEY information must be present in the header with correct value.");
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
            exceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private boolean isExceptional(String path) {
        return exceptionalPaths.stream()
                .anyMatch(it -> path.contains(it));

    }
}
