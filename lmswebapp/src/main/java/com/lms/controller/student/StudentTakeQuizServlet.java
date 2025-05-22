package com.lms.controller.student;

import com.lms.dao.QuizDAO;
import com.lms.model.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/student/quiz/take/*")
public class StudentTakeQuizServlet extends HttpServlet {
    private final QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // e.g. "/101"
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID required");
            return;
        }
        
        try {
            Long quizId = Long.parseLong(pathInfo.substring(1));
            Quiz quiz = quizDAO.findById(quizId);
            if (quiz == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
                return;
            }
            
            req.setAttribute("quiz", quiz);
            req.getRequestDispatcher("/student/takeQuiz.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
        }
    }
}
