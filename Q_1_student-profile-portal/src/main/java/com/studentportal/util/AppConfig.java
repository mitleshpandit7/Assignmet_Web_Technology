package com.studentportal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Loads application settings from db.properties.
 *
 * Default location : src/main/resources/db.properties (on the classpath).
 * Optional override : set the JVM property -Dportal.config=/path/to/file.properties
 *                     or the environment variable PORTAL_CONFIG.
 */
public final class AppConfig {

    private static volatile Properties cached;

    private AppConfig() {
    }

    private static Properties props() {
        Properties p = cached;
        if (p == null) {
            synchronized (AppConfig.class) {
                p = cached;
                if (p == null) {
                    p = load();
                    cached = p;
                }
            }
        }
        return p;
    }

    private static Properties load() {
        Properties p = new Properties();
        String external = System.getProperty("portal.config");
        if (external == null || external.trim().isEmpty()) {
            external = System.getenv("PORTAL_CONFIG");
        }
        try {
            if (external != null && !external.trim().isEmpty()) {
                try (Reader r = Files.newBufferedReader(Paths.get(external.trim()), StandardCharsets.UTF_8)) {
                    p.load(r);
                }
            } else {
                try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (in == null) {
                        throw new IllegalStateException("db.properties was not found on the classpath "
                                + "(expected at src/main/resources/db.properties).");
                    }
                    try (Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        p.load(r);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read configuration file: " + e.getMessage(), e);
        }
        return p;
    }

    private static String required(String key) {
        String v = props().getProperty(key);
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalStateException("Missing required setting '" + key + "' in db.properties");
        }
        return v.trim();
    }

    public static String dbUrl() {
        return required("db.url");
    }

    public static String dbUser() {
        return required("db.username");
    }

    /** Password may legitimately be empty (e.g. local MySQL without a password). */
    public static String dbPassword() {
        String v = props().getProperty("db.password");
        return v == null ? "" : v.trim();
    }

    public static Path uploadDir() {
        return Paths.get(required("upload.dir"));
    }

    public static int sessionTimeoutMinutes() {
        String v = props().getProperty("session.timeout.minutes", "30").trim();
        try {
            int m = Integer.parseInt(v);
            return m > 0 ? m : 30;
        } catch (NumberFormatException e) {
            return 30;
        }
    }
}
