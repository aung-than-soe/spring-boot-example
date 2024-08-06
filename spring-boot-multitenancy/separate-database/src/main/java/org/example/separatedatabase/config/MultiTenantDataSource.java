package org.example.separatedatabase.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Component
public class MultiTenantDataSource extends AbstractRoutingDataSource {

    private final TenantIdentifierResolver tenantIdentifierResolver;

    @Override
    protected Object determineCurrentLookupKey() {
        return this.tenantIdentifierResolver.resolveCurrentTenantIdentifier();
    }

    public MultiTenantDataSource(Environment environment, TenantIdentifierResolver tenantIdentifierResolver, TenantProperties tenantProperties) {
        this.tenantIdentifierResolver = tenantIdentifierResolver;
        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();
        for (Map.Entry<String, TenantProperties.CustomDatasourceProperties> entry: tenantProperties.getTenants().entrySet()) {
            HikariDataSource datasource = DataSourceBuilder
                    .create()
                    .url(entry.getValue().getUrl())
                    .username(entry.getValue().getUsername())
                    .password(entry.getValue().getPassword())
                    .driverClassName(entry.getValue().getDriverClassName())
                    .type(HikariDataSource.class)
                    .build();
            targetDataSources.put(entry.getKey(), datasource);
        }
        String defaultTenantIdentifier = environment.getProperty("tenant.default");
        log.info("Default tenant identifier: {}", defaultTenantIdentifier);
        TenantContext.setCurrentTenant(defaultTenantIdentifier);
        super.setDefaultTargetDataSource(targetDataSources.get(defaultTenantIdentifier));
        super.setTargetDataSources(targetDataSources);
    }

}
