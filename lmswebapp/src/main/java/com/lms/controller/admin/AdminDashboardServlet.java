package com.lms.controller.admin;

import com.lms.dao.CourseDAO;
import com.lms.dao.UserDAO;
import com.lms.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get counts for dashboard stats
        int userCount = userDAO.count();
        int courseCount = courseDAO.count();
        
        List<User> allUsers = userDAO.findAll();
        int activeStudentCount = 0;
        int activeTeacherCount = 0;
        
        for (User user : allUsers) {
            if (user.isActive()) {
                if (user.getRole() == User.Role.STUDENT) {
                    activeStudentCount++;
                } else if (user.getRole() == User.Role.TEACHER) {
                    activeTeacherCount++;
                }
            }
        }
        
        // Set attributes for the dashboard
        request.setAttribute("userCount", userCount);
        request.setAttribute("courseCount", courseCount);
        request.setAttribute("activeStudentCount", activeStudentCount);
        request.setAttribute("activeTeacherCount", activeTeacherCount);
        
        // Mock recent activity data (in a real app, this would come from a database)
        List<Map<String, String>> recentActivity = new ArrayList<>();
        
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("date", new Date().toString());
        activity1.put("user", "John Smith");
        activity1.put("action", "Created a new course: Java Programming");
        recentActivity.add(activity1);
        
        Map<String, String> activity2 = new HashMap<>();
        activity2.put("date", new Date().toString());
        activity2.put("user", "Jane Doe");
        activity2.put("action", "Enrolled in course: Web Development");
        recentActivity.add(activity2);
        
        Map<String, String> activity3 = new HashMap<>();
        activity3.put("date", new Date().toString());
        activity3.put("user", "Admin User");
        activity3.put("action", "Added a new teacher: Robert Johnson");
        recentActivity.add(activity3);
        
        request.setAttribute("recentActivity", recentActivity);
        
        // Forward to the dashboard JSP
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}