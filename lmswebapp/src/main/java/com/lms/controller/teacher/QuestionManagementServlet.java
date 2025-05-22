package com.lms.controller.teacher;

import com.lms.dao.AnswerDAO;
import com.lms.dao.QuestionDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Answer;
import com.lms.model.Question;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/teacher/questions/*")
public class QuestionManagementServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final QuizDAO quizDAO = new QuizDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Redirect to quizzes page
            response.sendRedirect(request.getContextPath() + "/teacher/quizzes");
        } else if (pathInfo.startsWith("/quiz/")) {
            // Show questions for a specific quiz
            try {
                Long quizId = Long.parseLong(pathInfo.substring(6));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null && quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    List<Question> questions = questionDAO.findByQuiz(quiz);
                    request.setAttribute("quiz", quiz);
                    request.setAttribute("questions", questions);
                    request.getRequestDispatcher("/teacher/questions.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to view questions for this quiz");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
            }
        } else if (pathInfo.equals("/create")) {
            // Show question creation form
            String quizIdParam = request.getParameter("quizId");
            if (quizIdParam != null && !quizIdParam.isEmpty()) {
                try {
                    Long quizId = Long.parseLong(quizIdParam);
                    Quiz quiz = quizDAO.findById(quizId);
                    
                    if (quiz != null && quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                        request.setAttribute("quiz", quiz);
                        request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to add questions to this quiz");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is required");
            }
        } else if (pathInfo.startsWith("/edit/")) {
            // Show question edit form
            try {
                Long questionId = Long.parseLong(pathInfo.substring(6));
                Question question = questionDAO.findById(questionId);
                
                if (question != null && question.getQuiz().getCourse().getTeacher().getId().equals(teacher.getId())) {
                    List<Answer> answers = answerDAO.findByQuestion(question);
                    request.setAttribute("question", question);
                    request.setAttribute("answers", answers);
                    request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit this question");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid question ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Handle question creation
            String quizIdStr = request.getParameter("quizId");
            String text = request.getParameter("text");
            String typeStr = request.getParameter("type");
            String pointsStr = request.getParameter("points");
            
            // Validate input
            if (quizIdStr == null || quizIdStr.trim().isEmpty() ||
                text == null || text.trim().isEmpty() ||
                typeStr == null || typeStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Quiz ID, question text, and question type are required");
                request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
                return;
            }
            
            try {
                Long quizId = Long.parseLong(quizIdStr);
                Quiz quiz = quizDAO.findById(quizId);
                
                // Verify the quiz belongs to the teacher
                if (quiz == null || !quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to add questions to this quiz");
                    return;
                }
                
                // Create new question
                Question question = new Question();
                question.setText(text);
                question.setQuiz(quiz);
                question.setType(Question.QuestionType.valueOf(typeStr));
                
                if (pointsStr != null && !pointsStr.trim().isEmpty()) {
                    question.setPoints(Integer.parseInt(pointsStr));
                }
                
                questionDAO.create(question);
                
                // Handle answers based on question type
                if (question.getType() == Question.QuestionType.MULTIPLE_CHOICE || 
                    question.getType() == Question.QuestionType.TRUE_FALSE) {
                    
                    String[] answerTexts = request.getParameterValues("answerText");
                    String[] correctAnswers = request.getParameterValues("correctAnswer");
                    
                    if (answerTexts != null) {
                        for (int i = 0; i < answerTexts.length; i++) {
                            if (answerTexts[i] != null && !answerTexts[i].trim().isEmpty()) {
                                Answer answer = new Answer();
                                answer.setText(answerTexts[i]);
                                answer.setQuestion(question);
                                
                                // Check if this answer is marked as correct
                                if (correctAnswers != null) {
                                    for (String correctIndex : correctAnswers) {
                                        if (String.valueOf(i).equals(correctIndex)) {
                                            answer.setCorrect(true);
                                            break;
                                        }
                                    }
                                }
                                
                                answerDAO.create(answer);
                            }
                        }
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/teacher/questions/quiz/" + quizId);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid quiz ID or points value");
                request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid question type");
                request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
            }
        } else if (pathInfo.startsWith("/edit/")) {
            // Handle question update
            try {
                Long questionId = Long.parseLong(pathInfo.substring(6));
                Question question = questionDAO.findById(questionId);
                
                if (question != null && question.getQuiz().getCourse().getTeacher().getId().equals(teacher.getId())) {
                    String text = request.getParameter("text");
                    String typeStr = request.getParameter("type");
                    String pointsStr = request.getParameter("points");
                    
                    // Validate input
                    if (text == null || text.trim().isEmpty() ||
                        typeStr == null || typeStr.trim().isEmpty()) {
                        
                        request.setAttribute("error", "Question text and question type are required");
                        request.setAttribute("question", question);
                        List<Answer> answers = answerDAO.findByQuestion(question);
                        request.setAttribute("answers", answers);
                        request.getRequestDispatcher("/teacher/question-form.jsp").forward(request, response);
                        return;
                    }
                    
                    // Update question
                    question.setText(text);
                    question.setType(Question.QuestionType.valueOf(typeStr));
                    
                    if (pointsStr != null && !pointsStr.trim().isEmpty()) {
                        question.setPoints(Integer.parseInt(pointsStr));
                    }
                    
                    questionDAO.update(question);
                    
                    // Handle answers based on question type
                    if (question.getType() == Question.QuestionType.MULTIPLE_CHOICE || 
                        question.getType() == Question.QuestionType.TRUE_FALSE) {
                        
                        // Delete existing answers
                        List<Answer> existingAnswers = answerDAO.findByQuestion(question);
                        for (Answer answer : existingAnswers) {
                            answerDAO.delete(answer);
                        }
                        
                        // Create new answers
                        String[] answerTexts = request.getParameterValues("answerText");
                        String[] correctAnswers = request.getParameterValues("correctAnswer");
                        
                        if (answerTexts != null) {
                            for (int i = 0; i < answerTexts.length; i++) {
                                if (answerTexts[i] != null && !answerTexts[i].trim().isEmpty()) {
                                    Answer answer = new Answer();
                                    answer.setText(answerTexts[i]);
                                    answer.setQuestion(question);
                                    
                                    // Check if this answer is marked as correct
                                    if (correctAnswers != null) {
                                        for (String correctIndex : correctAnswers) {
                                            if (String.valueOf(i).equals(correctIndex)) {
                                                answer.setCorrect(true);
                                                break;
                                            }
                                        }
                                    }
                                    
                                    answerDAO.create(answer);
                                }
                            }
                        }
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/teacher/questions/quiz/" + question.getQuiz().getId());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit this question");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid question ID or points value");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid question type");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Handle question deletion
            try {
                Long questionId = Long.parseLong(pathInfo.substring(8));
                Question question = questionDAO.findById(questionId);
                
                if (question != null && question.getQuiz().getCourse().getTeacher().getId().equals(teacher.getId())) {
                    Long quizId = question.getQuiz().getId();
                    questionDAO.delete(question);
                    response.sendRedirect(request.getContextPath() + "/teacher/questions/quiz/" + quizId);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to delete this question");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid question ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}