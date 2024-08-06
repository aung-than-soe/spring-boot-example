package org.example.separateschema.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.info("Get connection for tenant : {}", tenantIdentifier);
        Connection connection = this.getAnyConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.info("Release connection for tenant {}", tenantIdentifier);
        connection.setSchema(tenantIdentifier);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
        try {
            return getAnyConnection().isWrapperFor(unwrapType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> unwrapType) {
        try {
            return getAnyConnection().unwrap(unwrapType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public void addSchema(List<String> names) {
//        try {
//            Connection connection = this.getConnection(TenantContext.DEFAULT_TENANT);
//            StringBuilder builder = new StringBuilder();
//            for (String name : names) {
//                builder.append(String.format("CREATE SCHEMA IF NOT EXISTS %s;", name));
//                String scripts = this.loadScripts();
//                builder.append(String.format(scripts, name, name, name));
//            }
//            connection.createStatement()
//                    .execute(builder.toString());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private String loadScripts() throws IOException {
//        ClassPathResource resource = new ClassPathResource("tables.sql");
//        StringBuilder fileContents = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                fileContents.append(line).append("\n");
//            }
//        }
//        return fileContents.toString();
//    }
}
