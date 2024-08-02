package org.example.separatedatabase.config;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TenantProvider {
    A("client-a"), B("client-b");

    final String name;
    TenantProvider(String name) {
        this.name = name;
    }
}
