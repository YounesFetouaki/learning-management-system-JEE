package com.lms.controller.student;

import com.lms.dao.CourseDAO;
import com.lms.dao.UserDAO;
import com.lms.model.Course;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/courses")
public class StudentCoursesServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");

        try {
            // Fetch full user from DB by ID
            User student = userDAO.findById(sessionUser.getId());
            if (student == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            List<Course> courses = courseDAO.findCoursesByStudent(student);
            req.setAttribute("courses", courses);
            req.setAttribute("student", student); // to show profile info if needed

            req.getRequestDispatcher("/student/courses.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading student courses", e);
        }
    }
}
