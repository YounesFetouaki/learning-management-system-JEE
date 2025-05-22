package com.lms.controller.student;

import com.lms.dao.GradeDAO;
import com.lms.model.Grade;
import com.lms.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/grades")
public class StudentGradesServlet extends HttpServlet {

    private final GradeDAO gradeDAO = new GradeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User student = (User) session.getAttribute("user");

        try {
            // Fetch all grades for the logged-in student
            List<Grade> grades = gradeDAO.findByStudent(student);
            req.setAttribute("grades", grades);

            // Optionally calculate average grade
            Double avgGrade = gradeDAO.calculateAverageGradeForStudent(student);
            req.setAttribute("averageGrade", avgGrade);

            req.getRequestDispatcher("/student/grades.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error fetching grades for student", e);
        }
    }
}
