package org.example.separatedatabase.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"org.example.*.repository"})
public class PersistenceConfiguration {

    @Value("${tenant.default}")
    private String defaultTenant;
    private final TenantProperties tenantProperties;

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = tenantProperties.getTenants().entrySet().stream()
                .map((entry) -> {
                    HikariDataSource datasource = DataSourceBuilder
                            .create()
                            .url(entry.getValue().getUrl())
                            .username(entry.getValue().getUsername())
                            .password(entry.getValue().getPassword())
                            .driverClassName(entry.getValue().getDriverClassName())
                            .type(HikariDataSource.class)
                            .build();
                    return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), datasource);
                }).collect(Collectors.toUnmodifiableMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));

        AbstractRoutingDataSource dataSource = new MultiTenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, EntityManagerFactoryBuilder builder, HibernateProperties hibernateProperties, JpaProperties jpaProperties) {
        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put(AvailableSettings.SHOW_SQL, jpaProperties.isShowSql());
        jpaPropertiesMap.put(AvailableSettings.HBM2DDL_AUTO, hibernateProperties.getDdlAuto());
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(dataSource)
                .packages("org.example.*")
                .properties(jpaPropertiesMap)
                .persistenceUnit("multi-tenant")
                .build();

        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(jpaPropertiesMap);
        return entityManagerFactory;
    }

}
