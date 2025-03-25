package com.capit.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class GatewayFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String gatewayHeader = request.getHeader("X-Gateway-Auth");
        if (gatewayHeader == null || !gatewayHeader.equals("true")) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Requests allowed only from the API gateway");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
