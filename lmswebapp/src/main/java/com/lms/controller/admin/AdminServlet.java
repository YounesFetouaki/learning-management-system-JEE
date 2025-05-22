package com.lms.controller.admin;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/users")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            showCreateForm(request, response);
        } else if ("edit".equals(action)) {
            showEditForm(request, response);
        } else if ("delete".equals(action)) {
            deleteUser(request, response);
        } else {
            listUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("toggle-status".equals(action)) {
            toggleUserStatus(request, response);
        } else {
            saveUser(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("users", userDAO.findAllUsers());
        request.getRequestDispatcher("/admin/user-list.jsp")
               .forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("user", null);
        request.getRequestDispatcher("/admin/user-form.jsp")
               .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long userId = Long.parseLong(request.getParameter("id"));
        request.setAttribute("user", userDAO.findById(userId));
        request.getRequestDispatcher("/admin/user-form.jsp")
               .forward(request, response);
    }

    private void saveUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long id = request.getParameter("id") != null && !request.getParameter("id").isEmpty() 
                ? Long.parseLong(request.getParameter("id")) : null;
        
        User user = new User();
        user.setId(id);
        user.setEmail(request.getParameter("email"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setRole(User.Role.valueOf(request.getParameter("role")));
        user.setActive(request.getParameter("active") != null);
        
        // Only update password if it's provided (for new users or password changes)
        if (request.getParameter("password") != null && !request.getParameter("password").isEmpty()) {
            // In a real app, you should hash the password before storing
            user.setPasswordHash(request.getParameter("password"));
        } else if (id == null) {
            // New user must have a password
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is required for new users");
            return;
        }
        
        userDAO.saveOrUpdate(user);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long userId = Long.parseLong(request.getParameter("id"));
        userDAO.delete(userId);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private void toggleUserStatus(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long userId = Long.parseLong(request.getParameter("id"));
        userDAO.toggleUserStatus(userId);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}