package org.example.sharedschema.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

public class TenantContext implements Serializable {

    public static final String TENANT_IDENTIFIER = "X-TenantID";

    public static final String DEFAULT_TENANT = "public";

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantContext.class);

    public static String getCurrentTenant() {
        String tenant = Objects.requireNonNullElse(CURRENT_TENANT.get(), DEFAULT_TENANT) ;
        LOGGER.info("Current tenant is {}", tenant);
        return tenant;
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
