package com.lms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for managing system properties
 */
public class SystemProperties {
    
    private static final String PROPERTIES_FILE = "system.properties";
    private static Properties properties = new Properties();
    private static boolean loaded = false;
    
    /**
     * Load properties from file
     */
    public static synchronized void loadProperties() {
        if (loaded) {
            return;
        }
        
        String catalinaBase = System.getProperty("catalina.base", ".");
        File propertiesFile = new File(catalinaBase + "/conf/" + PROPERTIES_FILE);
        
        if (propertiesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                properties.load(fis);
                loaded = true;
            } catch (IOException e) {
                System.err.println("Error loading system properties: " + e.getMessage());
            }
        } else {
            // Create default properties file
            try {
                propertiesFile.getParentFile().mkdirs();
                saveProperties();
                loaded = true;
            } catch (Exception e) {
                System.err.println("Error creating default system properties: " + e.getMessage());
            }
        }
    }
    
    /**
     * Save properties to file
     */
    public static synchronized void saveProperties() {
        String catalinaBase = System.getProperty("catalina.base", ".");
        File propertiesFile = new File(catalinaBase + "/conf/" + PROPERTIES_FILE);
        
        try (FileOutputStream fos = new FileOutputStream(propertiesFile)) {
            properties.store(fos, "LMS System Properties");
        } catch (IOException e) {
            System.err.println("Error saving system properties: " + e.getMessage());
        }
    }
    
    /**
     * Get a property value
     */
    public static String getProperty(String key, String defaultValue) {
        if (!loaded) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set a property value
     */
    public static void setProperty(String key, String value) {
        if (!loaded) {
            loadProperties();
        }
        properties.setProperty(key, value);
    }
    
    /**
     * Get all properties
     */
    public static Properties getProperties() {
        if (!loaded) {
            loadProperties();
        }
        return properties;
    }
}