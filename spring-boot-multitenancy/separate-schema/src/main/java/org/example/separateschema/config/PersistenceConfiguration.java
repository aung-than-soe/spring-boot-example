package org.example.separateschema.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(basePackages = {"org.example.*"})
public class PersistenceConfiguration {

    private final JpaProperties jpaProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder,
                                                                       DataSource dataSource, MultiTenantConnectionProvider<String> multiTenantConnectionProviderImpl, CurrentTenantIdentifierResolver<String> currentTenantIdentifierResolverImpl, HibernateProperties hibernateProperties
    ) {
        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
        jpaPropertiesMap.put(AvailableSettings.SHOW_SQL, jpaProperties.isShowSql());
        jpaPropertiesMap.put(AvailableSettings.HBM2DDL_AUTO, hibernateProperties.getDdlAuto());

        LocalContainerEntityManagerFactoryBean em = factoryBuilder.dataSource(dataSource)
                .packages("org.example.separateschema.*")
                .persistenceUnit("separate-schema")
                .build();
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(jpaPropertiesMap);
        return em;
    }
}
