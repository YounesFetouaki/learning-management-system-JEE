package com.lms.controller.student;

import com.lms.dao.UserDAO;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/profile")
public class StudentProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get existing session, don't create new one if none
        HttpSession session = req.getSession(false);
        System.out.println("Session: " + session);

        if (session != null) {
            System.out.println("User in session: " + session.getAttribute("user"));
        }

        System.out.println("Requested path: " + req.getRequestURI());

        // Redirect to login if no session or user attribute
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Get logged in user from session
        User loggedInUser = (User) session.getAttribute("user");

        // Fetch fresh user data from DB by ID
        User student = userDAO.findById(loggedInUser.getId());
        System.out.println("User fetched from DB: " + student);

        if (student == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        // Set student as request attribute for JSP
        req.setAttribute("student", student);

        // Forward to JSP page
        req.getRequestDispatcher("/student/profile.jsp").forward(req, resp);
    }
}
