package com.shopsphere.ecommerce.config;

import com.shopsphere.ecommerce.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse =
                new ErrorResponse(
                        401,
                        "Authentication required"
                );

        String json = """
                {
                    "status": %d,
                    "message": "%s",
                    "timestamp": "%s"
                }
                """.formatted(
                errorResponse.getStatus(),
                errorResponse.getMessage(),
                errorResponse.getTimestamp()
        );

        response.getWriter().write(json);
    }
}