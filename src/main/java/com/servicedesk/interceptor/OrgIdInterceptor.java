package com.servicedesk.interceptor;

import com.servicedesk.context.OrganizationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class OrgIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String orgIdHeader = request.getHeader("X-Org-Id");

        if(orgIdHeader == null || orgIdHeader.isBlank()) {
            throw new IllegalArgumentException("Missing X-Org-Id header");
        }

        try {
            UUID orgId = UUID.fromString(orgIdHeader);
            OrganizationContext.setOrgId(orgId);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid X-Org-Id UUID format");
        }
        return true;
    }
}
