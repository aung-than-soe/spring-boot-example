package org.example.separatedatabase.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "datasource")
public class TenantProperties {

    private Map<String, Datasource> tenants;

    @Getter
    @Setter
    @ToString
    public static class Datasource {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }
}
