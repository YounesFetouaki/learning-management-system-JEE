package com.lms.controller.admin;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/profile")
public class AdminProfileServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");
        User admin = userDAO.findById(loggedInUser.getId());

        if (admin == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        req.setAttribute("admin", admin);
        req.getRequestDispatcher("/admin/profile.jsp").forward(req, resp);
    }
}
