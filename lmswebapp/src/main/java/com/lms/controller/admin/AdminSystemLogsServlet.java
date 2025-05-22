package com.lms.controller.admin;

import com.lms.dao.SystemLogDAO;
import com.lms.dao.UserDAO;
import com.lms.model.SystemLog;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/system/logs")
public class AdminSystemLogsServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        // Log this access
        LoggingUtility.auditEvent(admin.getId(), "VIEW_LOGS", "Admin accessed system logs", 
                "IP: " + request.getRemoteAddr());
        
        // Pagination parameters
        int page = 1;
        int limit = 50;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }
        
        // Filter parameters
        String category = request.getParameter("category");
        String level = request.getParameter("level");
        String userId = request.getParameter("userId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String keyword = request.getParameter("keyword");
        
        // Calculate offset
        int offset = (page - 1) * limit;
        
        // Get logs based on filters
        List<SystemLog> logs;
        long totalLogs;
        
        if (category != null && !category.isEmpty()) {
            logs = systemLogDAO.findByCategory(category, limit, offset);
            totalLogs = systemLogDAO.countLogsByCategory(category);
        } else if (level != null && !level.isEmpty()) {
            logs = systemLogDAO.findByLevel(level, limit, offset);
            totalLogs = systemLogDAO.countLogs(); // Simplified, should count by level
        } else if (userId != null && !userId.isEmpty()) {
            try {
                Long userIdLong = Long.parseLong(userId);
                User user = userDAO.findById(userIdLong);
                if (user != null) {
                    logs = systemLogDAO.findByUser(user, limit, offset);
                    totalLogs = systemLogDAO.countLogs(); // Simplified, should count by user
                } else {
                    logs = systemLogDAO.findAll();
                    totalLogs = systemLogDAO.countLogs();
                }
            } catch (NumberFormatException e) {
                logs = systemLogDAO.findAll();
                totalLogs = systemLogDAO.countLogs();
            }
        } else if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);
                // Add one day to end date to include the entire day
                endDate = new Date(endDate.getTime() + 86400000);
                
                logs = systemLogDAO.findByDateRange(startDate, endDate, limit, offset);
                totalLogs = systemLogDAO.countLogs(); // Simplified, should count by date range
            } catch (ParseException e) {
                logs = systemLogDAO.findAll();
                totalLogs = systemLogDAO.countLogs();
            }
        } else if (keyword != null && !keyword.isEmpty()) {
            logs = systemLogDAO.searchLogs(keyword, limit, offset);
            totalLogs = systemLogDAO.countLogs(); // Simplified, should count by keyword
        } else {
            logs = systemLogDAO.findAll();
            totalLogs = systemLogDAO.countLogs();
        }
        
        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalLogs / limit);
        
        // Set attributes for JSP
        request.setAttribute("logs", logs);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("category", category);
        request.setAttribute("level", level);
        request.setAttribute("userId", userId);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.setAttribute("keyword", keyword);
        
        // Get all users for filter dropdown
        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        
        // Forward to JSP
        request.getRequestDispatcher("/admin/system-logs.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Handle any POST actions like clearing logs or exporting
        String action = request.getParameter("action");
        
        if ("export".equals(action)) {
            // Handle log export
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"system_logs.csv\"");
            
            // Write CSV header
            response.getWriter().write("ID,Level,Category,User ID,Action,Message,Timestamp,IP Address\n");
            
            // Get all logs
            List<SystemLog> logs = systemLogDAO.findAll(); // Limit to 1000 for export
            
            // Write log data
            for (SystemLog log : logs) {
                response.getWriter().write(String.format("%d,%s,%s,%s,%s,\"%s\",%s,%s\n",
                        log.getId(),
                        log.getLevel(),
                        log.getCategory(),
                        log.getUserId() != null ? log.getUserId() : "",
                        log.getAction(),
                        log.getMessage().replace("\"", "\"\""), // Escape quotes for CSV
                        log.getTimestamp(),
                        log.getIpAddress() != null ? log.getIpAddress() : ""));
            }
        } else {
            // Redirect back to GET
            response.sendRedirect(request.getContextPath() + "/admin/system/logs");
        }
    }
}