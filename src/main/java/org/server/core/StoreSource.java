package org.server.core;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public enum StoreSource {

    REGISTRATION("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres"),
    ;

    private StoreSource(String url, String username, String password) {
        ds_ = new HikariDataSource();
        ds_.setJdbcUrl(url);
        ds_.setUsername(username);
        ds_.setPassword(password);
    }

    public DataSource dataSource() {
        return this.ds_;
    };

    private final HikariDataSource ds_;
}
