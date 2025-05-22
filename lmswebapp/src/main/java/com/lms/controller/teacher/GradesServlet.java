package com.lms.controller.teacher;

import com.lms.dao.GradeDAO;
import com.lms.dao.UserDAO;
import com.lms.dao.CourseDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Grade;
import com.lms.model.User;
import com.lms.model.Course;
import com.lms.model.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/grades")
public class GradesServlet extends HttpServlet {

    private GradeDAO gradeDAO;
    private UserDAO userDAO;
    private CourseDAO courseDAO;
    private QuizDAO quizDAO;

    @Override
    public void init() {
        gradeDAO = new GradeDAO();
        userDAO = new UserDAO();
        courseDAO = new CourseDAO();
        quizDAO = new QuizDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch(action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteGrade(request, response);
                break;
            default: // list
                listGrades(request, response);
                break;
        }
    }

    private void listGrades(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Grade> grades = gradeDAO.findAll();
        request.setAttribute("grades", grades);
        request.getRequestDispatcher("/teacher/grades.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("students", userDAO.findAllStudents());
        request.setAttribute("courses", courseDAO.findAll());
        request.setAttribute("quizzes", quizDAO.findAll());
        request.getRequestDispatcher("/teacher/grade-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Grade existingGrade = gradeDAO.findById(id);
        if (existingGrade == null) {
            response.sendRedirect(request.getContextPath() + "/teacher/grades");
            return;
        }
        request.setAttribute("grade", existingGrade);
        request.setAttribute("students", userDAO.findAllStudents());
        request.setAttribute("courses", courseDAO.findAll());
        request.setAttribute("quizzes", quizDAO.findAll());
        request.getRequestDispatcher("/teacher/grade-form.jsp").forward(request, response);
    }

    private void deleteGrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        gradeDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/teacher/grades");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idStr = request.getParameter("id");
        Long id = (idStr == null || idStr.isEmpty()) ? 0L : Long.parseLong(idStr);

        Long studentId = Long.parseLong(request.getParameter("studentId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long quizId = Long.parseLong(request.getParameter("quizId"));

        double score = Double.parseDouble(request.getParameter("score"));

        User student = userDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        Quiz quiz = quizDAO.findById(quizId);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setQuiz(quiz);
        grade.setScore(score);

        if (id == 0L) {
            gradeDAO.create(grade);
        } else {
            grade.setId(id);
            gradeDAO.update(grade);
        }

        response.sendRedirect(request.getContextPath() + "/teacher/grades");
    }
}
