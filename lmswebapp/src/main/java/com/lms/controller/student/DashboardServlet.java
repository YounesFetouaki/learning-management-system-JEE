package com.lms.controller.student;

import com.lms.dao.EnrollmentDAO;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        // Fetch number of enrolled courses for this student
        int enrolledCoursesCount = enrollmentDAO.findCoursesByStudent(user).size();

        // Set attribute to be accessed in JSP
        request.setAttribute("enrolledCoursesCount", enrolledCoursesCount);

        // TODO: Fetch and set other dashboard data (quizzes, grades, announcements...)

        request.getRequestDispatcher("/student/dashboard.jsp").forward(request, response);
    }
}
