package org.example.separatedatabase.config;


import java.io.Serializable;

public class TenantContext implements Serializable {

    public static final String TENANT_IDENTIFIER = "X-TenantID";

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
