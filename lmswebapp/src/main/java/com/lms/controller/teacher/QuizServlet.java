package com.lms.controller.teacher;

import com.lms.dao.CourseDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Question;
import com.lms.model.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@WebServlet("/teacher/quizzes")
public class QuizServlet extends HttpServlet {

    private QuizDAO quizDAO;
    private CourseDAO courseDAO;

    @Override
    public void init() {
        quizDAO = new QuizDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteQuiz(request, response);
                    break;
                case "list":
                default:
                    listQuizzes(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("insert".equals(action)) {
                insertQuiz(request, response);
            } else if ("update".equals(action)) {
                updateQuiz(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/teacher/quizzes");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listQuizzes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Quiz> quizzes = quizDAO.findAll();
        request.setAttribute("quizzes", quizzes);
        // Forward to JSP inside teacher folder
        request.getRequestDispatcher("/teacher/quiz_list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> courses = courseDAO.findAll();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/teacher/quiz_form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Quiz existingQuiz = quizDAO.findById(id);
        List<Course> courses = courseDAO.findAll();
        request.setAttribute("quiz", existingQuiz);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/teacher/quiz_form.jsp").forward(request, response);
    }

    private void insertQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getParameter("title"));
        quiz.setDescription(request.getParameter("description"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Course course = courseDAO.findById(courseId);
        quiz.setCourse(course);
        quiz.setTimeLimitMinutes(parseInteger(request.getParameter("timeLimitMinutes")));
        quiz.setPassingScore(parseInteger(request.getParameter("passingScore")));
        quiz.setActive("on".equals(request.getParameter("active")));
        // TODO: parse dates if needed (startDate, endDate)
        String[] questionTexts = request.getParameterValues("questionTexts");
        String[] questionTypes = request.getParameterValues("questionTypes");
        String[] questionPoints = request.getParameterValues("questionPoints");

        List<Question> questionList = new ArrayList<>();

        if (questionTexts != null) {
            for (int i = 0; i < questionTexts.length; i++) {
                Question q = new Question();
                q.setText(questionTexts[i]);
                q.setType(Question.QuestionType.valueOf(questionTypes[i]));
                q.setPoints(Integer.parseInt(questionPoints[i]));
                q.setQuiz(quiz); // assuming quiz object is available
                questionList.add(q);
            }
        }


        // Then persist them with the quiz
        quiz.setQuestions(new HashSet<>(questionList));


        quizDAO.save(quiz);
        response.sendRedirect(request.getContextPath() + "/teacher/quizzes");
    }

    private void updateQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Quiz quiz = quizDAO.findById(id);

        quiz.setTitle(request.getParameter("title"));
        quiz.setDescription(request.getParameter("description"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Course course = courseDAO.findById(courseId);
        quiz.setCourse(course);
        quiz.setTimeLimitMinutes(parseInteger(request.getParameter("timeLimitMinutes")));
        quiz.setPassingScore(parseInteger(request.getParameter("passingScore")));
        quiz.setActive("on".equals(request.getParameter("active")));
        // TODO: parse dates if needed
        String[] questionTexts = request.getParameterValues("questionTexts");
        String[] questionTypes = request.getParameterValues("questionTypes");
        String[] questionPoints = request.getParameterValues("questionPoints");
        List<Question> questionList = new ArrayList<>();

        if (questionTexts != null) {
            for (int i = 0; i < questionTexts.length; i++) {
                Question q = new Question();
                q.setText(questionTexts[i]);
                q.setType(Question.QuestionType.valueOf(questionTypes[i]));
                q.setPoints(Integer.parseInt(questionPoints[i]));
                q.setQuiz(quiz); // assuming quiz object is available
                questionList.add(q);
            }
        }

        // Then persist them with the quiz
        quiz.setQuestions(new HashSet<>(questionList));

        quizDAO.save(quiz);
        response.sendRedirect(request.getContextPath() + "/teacher/quizzes");
    }

    private void deleteQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        quizDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/teacher/quizzes");
    }

    private Integer parseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }
}
