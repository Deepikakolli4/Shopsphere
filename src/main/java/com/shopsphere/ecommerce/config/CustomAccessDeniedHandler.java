package com.shopsphere.ecommerce.config;

import com.shopsphere.ecommerce.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse =
                new ErrorResponse(
                        403,
                        "Access denied"
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