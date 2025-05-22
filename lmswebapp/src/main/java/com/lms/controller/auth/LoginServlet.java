package com.lms.controller.auth;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Redirect based on user role
            User user = (User) session.getAttribute("user");
            redirectBasedOnRole(user, response, request);
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = userDAO.findByEmail(email);
        if (user != null && userDAO.authenticate(email, password)) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userRole", user.getRole().toString());
            
            // Redirect based on user role
            redirectBasedOnRole(user, response, request);
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    private void redirectBasedOnRole(User user, HttpServletResponse response, HttpServletRequest request) throws IOException {
        switch (user.getRole()) {
            case ADMIN:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                break;
            case TEACHER:
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                break;
            case STUDENT:
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login");
                break;
        }
    }
}