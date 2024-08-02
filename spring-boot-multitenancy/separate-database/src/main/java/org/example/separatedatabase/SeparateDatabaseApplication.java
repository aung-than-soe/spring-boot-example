package org.example.separatedatabase;

import org.example.separatedatabase.config.TenantProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {TenantProperties.class})
@SpringBootApplication
public class SeparateDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeparateDatabaseApplication.class, args);
    }

}
