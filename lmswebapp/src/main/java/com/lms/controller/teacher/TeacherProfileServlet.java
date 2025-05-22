package com.lms.controller.teacher;

import com.lms.dao.UserDAO;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/teacher/profile")
public class TeacherProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // Fetch fresh user data from DB by ID
        User teacher = userDAO.findById(loggedInUser.getId());

        if (teacher == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        req.setAttribute("teacher", teacher);

        req.getRequestDispatcher("/teacher/profile.jsp").forward(req, resp);
    }
}
