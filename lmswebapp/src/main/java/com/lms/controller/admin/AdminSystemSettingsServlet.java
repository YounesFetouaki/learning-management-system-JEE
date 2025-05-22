package com.lms.controller.admin;

import com.lms.model.User;
import com.lms.util.LoggingUtility;
import com.lms.util.LoggingUtility.LogCategory;
import com.lms.util.SystemProperties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet("/admin/system/settings")
public class AdminSystemSettingsServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        // Log this access
        LoggingUtility.auditEvent(admin.getId(), "VIEW_SYSTEM_SETTINGS", "Admin accessed system settings", 
                "IP: " + request.getRemoteAddr());
        
        // Get current system settings
        Map<String, String> settings = new HashMap<>();
        
        // Application settings
        settings.put("app.name", SystemProperties.getProperty("app.name", "Learning Management System"));
        settings.put("app.version", SystemProperties.getProperty("app.version", "1.0.0"));
        settings.put("app.url", SystemProperties.getProperty("app.url", request.getRequestURL().toString()));
        
        // Email settings
        settings.put("mail.enabled", SystemProperties.getProperty("mail.enabled", "false"));
        settings.put("mail.smtp.host", SystemProperties.getProperty("mail.smtp.host", ""));
        settings.put("mail.smtp.port", SystemProperties.getProperty("mail.smtp.port", "587"));
        settings.put("mail.from", SystemProperties.getProperty("mail.from", ""));
        
        // Security settings
        settings.put("security.password.min_length", SystemProperties.getProperty("security.password.min_length", "8"));
        settings.put("security.session.timeout", SystemProperties.getProperty("security.session.timeout", "30"));
        settings.put("security.login.max_attempts", SystemProperties.getProperty("security.login.max_attempts", "5"));
        
        // File upload settings
        settings.put("upload.max_size", SystemProperties.getProperty("upload.max_size", "10485760")); // 10MB
        settings.put("upload.allowed_types", SystemProperties.getProperty("upload.allowed_types", "pdf,doc,docx,ppt,pptx,xls,xlsx,txt,zip,jpg,png,gif"));
        
        // System information
        Properties systemProps = System.getProperties();
        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("java.version", systemProps.getProperty("java.version"));
        systemInfo.put("java.vendor", systemProps.getProperty("java.vendor"));
        systemInfo.put("os.name", systemProps.getProperty("os.name"));
        systemInfo.put("os.version", systemProps.getProperty("os.version"));
        systemInfo.put("user.timezone", systemProps.getProperty("user.timezone"));
        systemInfo.put("file.encoding", systemProps.getProperty("file.encoding"));
        systemInfo.put("catalina.home", systemProps.getProperty("catalina.home", "N/A"));
        
        // Set attributes for JSP
        request.setAttribute("settings", settings);
        request.setAttribute("systemInfo", systemInfo);
        
        // Forward to JSP
        request.getRequestDispatcher("/admin/system-settings.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        // Get parameters
        String appName = request.getParameter("app.name");
        String mailEnabled = request.getParameter("mail.enabled");
        String mailSmtpHost = request.getParameter("mail.smtp.host");
        String mailSmtpPort = request.getParameter("mail.smtp.port");
        String mailFrom = request.getParameter("mail.from");
        String securityPasswordMinLength = request.getParameter("security.password.min_length");
        String securitySessionTimeout = request.getParameter("security.session.timeout");
        String securityLoginMaxAttempts = request.getParameter("security.login.max_attempts");
        String uploadMaxSize = request.getParameter("upload.max_size");
        String uploadAllowedTypes = request.getParameter("upload.allowed_types");
        
        // Update settings
        if (appName != null && !appName.isEmpty()) {
            SystemProperties.setProperty("app.name", appName);
        }
        
        if (mailEnabled != null) {
            SystemProperties.setProperty("mail.enabled", mailEnabled);
        }
        
        if (mailSmtpHost != null) {
            SystemProperties.setProperty("mail.smtp.host", mailSmtpHost);
        }
        
        if (mailSmtpPort != null && !mailSmtpPort.isEmpty()) {
            SystemProperties.setProperty("mail.smtp.port", mailSmtpPort);
        }
        
        if (mailFrom != null) {
            SystemProperties.setProperty("mail.from", mailFrom);
        }
        
        if (securityPasswordMinLength != null && !securityPasswordMinLength.isEmpty()) {
            SystemProperties.setProperty("security.password.min_length", securityPasswordMinLength);
        }
        
        if (securitySessionTimeout != null && !securitySessionTimeout.isEmpty()) {
            SystemProperties.setProperty("security.session.timeout", securitySessionTimeout);
        }
        
        if (securityLoginMaxAttempts != null && !securityLoginMaxAttempts.isEmpty()) {
            SystemProperties.setProperty("security.login.max_attempts", securityLoginMaxAttempts);
        }
        
        if (uploadMaxSize != null && !uploadMaxSize.isEmpty()) {
            SystemProperties.setProperty("upload.max_size", uploadMaxSize);
        }
        
        if (uploadAllowedTypes != null) {
            SystemProperties.setProperty("upload.allowed_types", uploadAllowedTypes);
        }
        
        // Save settings
        SystemProperties.saveProperties();
        
        // Log the action
        LoggingUtility.auditEvent(admin.getId(), "UPDATE_SYSTEM_SETTINGS", "Admin updated system settings", 
                "IP: " + request.getRemoteAddr());
        
        // Set success message
        request.getSession().setAttribute("success", "System settings updated successfully");
        
        // Redirect back to settings page
        response.sendRedirect(request.getContextPath() + "/admin/system/settings");
    }
}