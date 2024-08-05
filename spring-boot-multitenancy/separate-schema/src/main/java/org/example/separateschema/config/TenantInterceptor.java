package org.example.separateschema.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class TenantInterceptor implements HandlerInterceptor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TenantInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantID = request.getHeader(TenantContext.TENANT_IDENTIFIER);
        LOGGER.info("Current Tenant ID: {}", tenantID);
        TenantContext.setCurrentTenant(Objects.requireNonNullElse(tenantID, TenantContext.DEFAULT_TENANT));
        return true;
    }
}
