package com.servicedesk.security;

import com.servicedesk.context.OrganizationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/swagger-ui.html",
            "/swagger-ui/",
            "/api-docs/",
            "/v3/api-docs/",
            "/swagger-resources/",
            "/webjars/"
    );
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String path = request.getRequestURI();

            // Skip header check for Swagger endpoints
            if (isExcludedPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                JwtUser user = jwtService.parse(token);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                OrganizationContext.setOrgId(user.getOrgId());
            }
            filterChain.doFilter(request, response);
        }
        finally {
            OrganizationContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(path::startsWith);
    }
}
