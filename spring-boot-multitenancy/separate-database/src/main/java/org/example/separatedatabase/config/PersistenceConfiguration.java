package org.example.separatedatabase.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"org.example.separatedatabase.repository"})
public class PersistenceConfiguration {

    private final MultiTenantDataSource multiTenantDataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, JpaProperties jpaProperties, CurrentTenantIdentifierResolver<String> identifierResolver, MultiTenantConnectionProvider<String> connectionProvider) {
        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.forEach((key, value) -> log.info("jpa properties key: {}, value: {}", key, value));

        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, identifierResolver);
        jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);

        jpaPropertiesMap.put(AvailableSettings.HBM2DDL_AUTO, jpaProperties.isGenerateDdl() ? "update" : "none");
        jpaPropertiesMap.put(AvailableSettings.SHOW_SQL, jpaProperties.isShowSql());

        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(this.multiTenantDataSource)
                .packages("org.example.separatedatabase.domain")
                .properties(jpaPropertiesMap)
                .persistenceUnit("multi-tenant")
                .build();
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaPropertyMap(jpaPropertiesMap);
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return entityManagerFactory;
    }

}
