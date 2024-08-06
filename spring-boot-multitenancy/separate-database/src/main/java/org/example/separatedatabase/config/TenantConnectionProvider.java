package org.example.separatedatabase.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
@RequiredArgsConstructor
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {

    private static final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    private final DataSource dataSource;

    @Override
    protected DataSource selectAnyDataSource() {
        return ((AbstractRoutingDataSource)this.dataSource).getResolvedDataSources().values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return ((AbstractRoutingDataSource)this.dataSource).getResolvedDataSources().get(tenantIdentifier);
    }

}
