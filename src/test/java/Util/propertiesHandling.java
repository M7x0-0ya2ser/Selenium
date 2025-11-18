package Util;

import java.io.InputStream;
import java.util.Properties;

public class propertiesHandling {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = propertiesHandling.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties file not found in classpath.");
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database configuration.");
        }
    }

    public static String getDbUrl(String zone) {
        return zone.equalsIgnoreCase("qc") ? properties.getProperty("database.qc.url")
                : properties.getProperty("database.support.url");
    }

    public static String getUser() {
        return properties.getProperty("database.user");
    }

    public static String getPassword() {
        return properties.getProperty("database.password");
    }

    public static String getDriver() {
        return properties.getProperty("database.driver");
    }
}
