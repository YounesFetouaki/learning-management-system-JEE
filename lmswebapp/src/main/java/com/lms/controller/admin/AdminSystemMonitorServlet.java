package com.lms.controller.admin;

import com.lms.dao.CourseDAO;
import com.lms.dao.QuizDAO;
import com.lms.dao.SubmissionDAO;
import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.util.LoggingUtility;
import com.lms.util.LoggingUtility.LogCategory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/system/monitor")
public class AdminSystemMonitorServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final SubmissionDAO submissionDAO = new SubmissionDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        // Log this access
        LoggingUtility.auditEvent(admin.getId(), "VIEW_SYSTEM_MONITOR", "Admin accessed system monitor", 
                "IP: " + request.getRemoteAddr());
        
        // Get system metrics
        Map<String, Object> systemMetrics = getSystemMetrics();
        
        // Get application metrics
        Map<String, Object> appMetrics = getApplicationMetrics();
        
        // Set attributes for JSP
        request.setAttribute("systemMetrics", systemMetrics);
        request.setAttribute("appMetrics", appMetrics);
        
        // Forward to JSP
        request.getRequestDispatcher("/admin/system-monitor.jsp").forward(request, response);
    }
    
    /**
     * Get system metrics using JMX
     */
    private Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Memory information
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryBean.getHeapMemoryUsage().getMax();
        long nonHeapUsed = memoryBean.getNonHeapMemoryUsage().getUsed();
        
        metrics.put("heapUsed", formatSize(heapUsed));
        metrics.put("heapMax", formatSize(heapMax));
        metrics.put("heapUsedPercentage", (int) ((heapUsed * 100) / heapMax));
        metrics.put("nonHeapUsed", formatSize(nonHeapUsed));
        
        // CPU information
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        metrics.put("availableProcessors", osBean.getAvailableProcessors());
        metrics.put("systemLoadAverage", osBean.getSystemLoadAverage());
        
        // Runtime information
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeBean.getUptime();
        metrics.put("uptime", formatDuration(uptime));
        metrics.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(runtimeBean.getStartTime())));
        metrics.put("jvmName", runtimeBean.getVmName());
        metrics.put("jvmVersion", runtimeBean.getVmVersion());
        
        return metrics;
    }
    
    /**
     * Get application-specific metrics
     */
    private Map<String, Object> getApplicationMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Count entities
        long userCount = userDAO.count();
        long courseCount = courseDAO.count();
        long quizCount = quizDAO.count();
        long submissionCount = submissionDAO.countLogs();
        
        metrics.put("userCount", userCount);
        metrics.put("courseCount", courseCount);
        metrics.put("quizCount", quizCount);
        metrics.put("submissionCount", submissionCount);
        
        // Active sessions (simplified, in a real app would use SessionListener)
        metrics.put("activeSessions", getServletContext().getAttribute("activeSessions") != null ? 
                getServletContext().getAttribute("activeSessions") : 0);
        
        return metrics;
    }
    
    /**
     * Format byte size to human-readable format
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
    
    /**
     * Format duration in milliseconds to human-readable format
     */
    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%d days, %d hours", days, hours % 24);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%d minutes, %d seconds", minutes, seconds % 60);
        } else {
            return String.format("%d seconds", seconds);
        }
    }
}