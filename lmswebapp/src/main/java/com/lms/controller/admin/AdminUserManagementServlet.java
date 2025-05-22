package com.lms.controller.admin;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/users/*")
public class AdminUserManagementServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all users
            List<User> users = userDAO.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else if (pathInfo.equals("/create")) {
            // Show user creation form
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show user edit form
            try {
                Long userId = Long.parseLong(pathInfo.substring(6));
                User user = userDAO.findById(userId);
                
                if (user != null) {
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Handle user creation
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String roleStr = request.getParameter("role");
            String activeStr = request.getParameter("active");
            
            // Validate input
            if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                roleStr == null || roleStr.trim().isEmpty()) {
                
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                return;
            }
            
            // Check if email already exists
            User existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                request.setAttribute("error", "Email already exists");
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                return;
            }
            
            // Create new user
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPasswordHash(password); // In a real app, this would be hashed
            user.setRole(User.Role.valueOf(roleStr));
            user.setActive(activeStr != null && activeStr.equals("on"));
            
            userDAO.create(user);
            
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else if (pathInfo.startsWith("/edit/")) {
            // Handle user update
            try {
                Long userId = Long.parseLong(pathInfo.substring(6));
                User user = userDAO.findById(userId);
                
                if (user != null) {
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    String roleStr = request.getParameter("role");
                    String activeStr = request.getParameter("active");
                    
                    // Validate input
                    if (firstName == null || firstName.trim().isEmpty() ||
                        lastName == null || lastName.trim().isEmpty() ||
                        email == null || email.trim().isEmpty() ||
                        roleStr == null || roleStr.trim().isEmpty()) {
                        
                        request.setAttribute("error", "First name, last name, email, and role are required");
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                        return;
                    }
                    
                    // Check if email already exists for another user
                    User existingUser = userDAO.findByEmail(email);
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        request.setAttribute("error", "Email already exists");
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                        return;
                    }
                    
                    // Update user
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    if (password != null && !password.trim().isEmpty()) {
                        user.setPasswordHash(password); // In a real app, this would be hashed
                    }
                    user.setRole(User.Role.valueOf(roleStr));
                    user.setActive(activeStr != null && activeStr.equals("on"));
                    
                    userDAO.update(user);
                    
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Handle user deletion
            try {
                Long userId = Long.parseLong(pathInfo.substring(8));
                User user = userDAO.findById(userId);
                
                if (user != null) {
                    userDAO.delete(user);
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}