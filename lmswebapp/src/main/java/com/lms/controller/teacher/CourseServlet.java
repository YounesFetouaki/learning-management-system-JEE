package com.lms.controller.teacher;

import com.lms.dao.CourseDAO;
import com.lms.model.Course;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/courses")
public class CourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User teacher = (session != null) ? (User) session.getAttribute("user") : null;

        if (teacher == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (action == null || action.equals("list")) {
            // List courses
            List<Course> courses = courseDAO.findByTeacher(teacher);
            req.setAttribute("courses", courses);
            req.getRequestDispatcher("/teacher/courses.jsp").forward(req, resp);

        } else if (action.equals("new")) {
            // Show create form
            req.getRequestDispatcher("/teacher/course_form.jsp").forward(req, resp);

        } else if (action.equals("edit")) {
            // Show edit form
            String idStr = req.getParameter("id");
            if (idStr == null) {
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
                return;
            }
            Long id = Long.parseLong(idStr);
            Course course = courseDAO.findById(id);
            if (course == null) {
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
                return;
            }
            req.setAttribute("course", course);
            req.getRequestDispatcher("/teacher/course_form.jsp").forward(req, resp);

        } else if (action.equals("delete")) {
            // Delete course
            String idStr = req.getParameter("id");
            if (idStr != null) {
                Long id = Long.parseLong(idStr);
                Course course = courseDAO.findById(id);
                if (course != null) {
                    courseDAO.deleteById(id); // fixed call to deleteById method
                }
            }
            resp.sendRedirect(req.getContextPath() + "/teacher/courses");
        } else {
            // Unknown action
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User teacher = (session != null) ? (User) session.getAttribute("user") : null;

        if (teacher == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter missing");
            return;
        }

        if (action.equals("create")) {
            // Create new course
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            boolean active = "on".equals(req.getParameter("active"));

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setActive(active);
            course.setTeacher(teacher);

            try {
                courseDAO.save(course);  // fixed call to save method
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
            } catch (Exception e) {
                req.setAttribute("error", "Error creating course: " + e.getMessage());
                req.getRequestDispatcher("/teacher/course_form.jsp").forward(req, resp);
            }

        } else if (action.equals("update")) {
            // Update existing course
            String idStr = req.getParameter("id");
            if (idStr == null) {
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
                return;
            }
            Long id = Long.parseLong(idStr);
            Course course = courseDAO.findById(id);
            if (course == null) {
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
                return;
            }

            course.setTitle(req.getParameter("title"));
            course.setDescription(req.getParameter("description"));
            course.setActive("on".equals(req.getParameter("active")));

            try {
                courseDAO.update(course);
                resp.sendRedirect(req.getContextPath() + "/teacher/courses");
            } catch (Exception e) {
                req.setAttribute("error", "Error updating course: " + e.getMessage());
                req.setAttribute("course", course);
                req.getRequestDispatcher("/teacher/course_form.jsp").forward(req, resp);
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}
