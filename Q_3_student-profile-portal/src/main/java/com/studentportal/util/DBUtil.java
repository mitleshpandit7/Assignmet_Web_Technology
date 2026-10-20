package com.studentportal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Creates JDBC connections using the settings in db.properties. */
public final class DBUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private DBUtil() {
    }

    /**
     * Opens a new connection. Callers MUST close it (use try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Explicit load: makes the driver available inside Tomcat's webapp class loader.
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver (" + DRIVER + ") not found. "
                    + "Check the mysql-connector-j dependency in pom.xml.", e);
        }
        return DriverManager.getConnection(AppConfig.dbUrl(), AppConfig.dbUser(), AppConfig.dbPassword());
    }
}
