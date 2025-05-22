package com.lms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;

/**
 * Utility class for centralized logging with additional context information.
 */
public class LoggingUtility {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtility.class);
    
    /**
     * Log levels
     */
    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }
    
    /**
     * Log categories for better organization
     */
    public enum LogCategory {
        SECURITY("SECURITY"),
        USER_ACTIVITY("USER_ACTIVITY"),
        SYSTEM("SYSTEM"),
        DATABASE("DATABASE"),
        PERFORMANCE("PERFORMANCE"),
        AUDIT("AUDIT");
        
        private final String value;
        
        LogCategory(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * Log a message with context information
     * 
     * @param level Log level
     * @param category Log category
     * @param userId User ID (if applicable)
     * @param action Action being performed
     * @param message Log message
     * @param details Additional details (optional)
     */
    public static void log(LogLevel level, LogCategory category, Long userId, String action, String message, String details) {
        try {
            // Set MDC context for structured logging
            MDC.put("category", category.getValue());
            MDC.put("timestamp", new Date().toString());
            MDC.put("action", action);
            
            if (userId != null) {
                MDC.put("userId", userId.toString());
            }
            
            String logMessage = buildLogMessage(message, details);
            
            // Log with appropriate level
            switch (level) {
                case INFO:
                    logger.info(logMessage);
                    break;
                case WARN:
                    logger.warn(logMessage);
                    break;
                case ERROR:
                    logger.error(logMessage);
                    break;
                case DEBUG:
                    logger.debug(logMessage);
                    break;
            }
        } finally {
            // Clear MDC context
            MDC.clear();
        }
    }
    
    /**
     * Log an info message
     */
    public static void info(LogCategory category, Long userId, String action, String message) {
        log(LogLevel.INFO, category, userId, action, message, null);
    }
    
    /**
     * Log a warning message
     */
    public static void warn(LogCategory category, Long userId, String action, String message) {
        log(LogLevel.WARN, category, userId, action, message, null);
    }
    
    /**
     * Log an error message
     */
    public static void error(LogCategory category, Long userId, String action, String message, Throwable throwable) {
        log(LogLevel.ERROR, category, userId, action, message, throwable != null ? throwable.toString() : null);
        if (throwable != null) {
            logger.error("Exception details:", throwable);
        }
    }
    
    /**
     * Log a security event
     */
    public static void securityEvent(Long userId, String action, String message) {
        log(LogLevel.INFO, LogCategory.SECURITY, userId, action, message, null);
    }
    
    /**
     * Log an audit event
     */
    public static void auditEvent(Long userId, String action, String message, String details) {
        log(LogLevel.INFO, LogCategory.AUDIT, userId, action, message, details);
    }
    
    /**
     * Build a formatted log message
     */
    private static String buildLogMessage(String message, String details) {
        StringBuilder sb = new StringBuilder(message);
        
        if (details != null && !details.isEmpty()) {
            sb.append(" | Details: ").append(details);
        }
        
        return sb.toString();
    }
}