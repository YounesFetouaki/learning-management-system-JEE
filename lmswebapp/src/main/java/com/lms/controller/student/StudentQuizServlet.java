package com.lms.controller.student;

import com.lms.dao.AnswerDAO;
import com.lms.dao.GradeDAO;
import com.lms.dao.QuestionDAO;
import com.lms.dao.QuizDAO;
import com.lms.dao.SubmissionDAO;
import com.lms.model.Answer;
import com.lms.model.Grade;
import com.lms.model.Question;
import com.lms.model.Quiz;
import com.lms.model.StudentAnswer;
import com.lms.model.Submission;
import com.lms.model.User;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/student/quiz/*")
public class StudentQuizServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final QuizDAO quizDAO = new QuizDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    private final SubmissionDAO submissionDAO = new SubmissionDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Redirect to courses page
            response.sendRedirect(request.getContextPath() + "/student/courses");
        } else if (pathInfo.startsWith("/take/")) {
            // Show quiz taking page
            try {
                Long quizId = Long.parseLong(pathInfo.substring(6));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null) {
                    // Check if quiz is active and within date range
                    Date currentDate = new Date();
                    boolean isAvailable = quiz.isActive() && 
                                         (quiz.getStartDate() == null || quiz.getStartDate().before(currentDate)) &&
                                         (quiz.getEndDate() == null || quiz.getEndDate().after(currentDate));
                    
                    if (!isAvailable) {
                        request.setAttribute("error", "This quiz is not currently available.");
                        request.getRequestDispatcher("/student/quiz-unavailable.jsp").forward(request, response);
                        return;
                    }
                    
                    // Check if student has already submitted this quiz
                    Submission existingSubmission = submissionDAO.findByStudentAndQuiz(student, quiz);
                    if (existingSubmission != null) {
                        request.setAttribute("error", "You have already submitted this quiz.");
                        request.setAttribute("submission", existingSubmission);
                        request.getRequestDispatcher("/student/quiz-already-submitted.jsp").forward(request, response);
                        return;
                    }
                    
                    // Get questions for the quiz
                    List<Question> questions = questionDAO.findByQuiz(quiz);
                    
                    // For each multiple choice or true/false question, get the answer options
                    Map<Long, List<Answer>> questionAnswers = new HashMap<>();
                    for (Question question : questions) {
                        if (question.getType() == Question.QuestionType.MULTIPLE_CHOICE || 
                            question.getType() == Question.QuestionType.TRUE_FALSE) {
                            questionAnswers.put(question.getId(), answerDAO.findByQuestion(question));
                        }
                    }
                    
                    request.setAttribute("quiz", quiz);
                    request.setAttribute("questions", questions);
                    request.setAttribute("questionAnswers", questionAnswers);
                    
                    // Set quiz start time in session
                    session.setAttribute("quizStartTime", new Date());
                    
                    request.getRequestDispatcher("/student/quiz-take.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
            }
        } else if (pathInfo.startsWith("/result/")) {
            // Show quiz result page
            try {
                Long submissionId = Long.parseLong(pathInfo.substring(8));
                Submission submission = submissionDAO.findById(submissionId);
                
                if (submission != null && submission.getStudent().getId().equals(student.getId())) {
                    // Get the grade for this submission
                    Grade grade = gradeDAO.findByStudentAndQuiz(student, submission.getQuiz());
                    
                    request.setAttribute("submission", submission);
                    request.setAttribute("grade", grade);
                    request.getRequestDispatcher("/student/quiz-result.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to view this submission");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid submission ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.startsWith("/submit/")) {
            // Handle quiz submission
            try {
                Long quizId = Long.parseLong(pathInfo.substring(8));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null) {
                    // Check if student has already submitted this quiz
                    Submission existingSubmission = submissionDAO.findByStudentAndQuiz(student, quiz);
                    if (existingSubmission != null) {
                        request.setAttribute("error", "You have already submitted this quiz.");
                        request.setAttribute("submission", existingSubmission);
                        request.getRequestDispatcher("/student/quiz-already-submitted.jsp").forward(request, response);
                        return;
                    }
                    
                    // Check time limit if applicable
                    Date quizStartTime = (Date) session.getAttribute("quizStartTime");
                    if (quizStartTime != null && quiz.getTimeLimitMinutes() != null) {
                        Date currentTime = new Date();
                        long elapsedTimeMinutes = (currentTime.getTime() - quizStartTime.getTime()) / (60 * 1000);
                        
                        if (elapsedTimeMinutes > quiz.getTimeLimitMinutes()) {
                            request.setAttribute("error", "Time limit exceeded. Your submission was not accepted.");
                            request.getRequestDispatcher("/student/quiz-time-exceeded.jsp").forward(request, response);
                            return;
                        }
                    }
                    
                    // Create submission
                    Submission submission = new Submission();
                    submission.setStudent(student);
                    submission.setQuiz(quiz);
                    submission.setSubmissionDate(new Date());
                    submissionDAO.create(submission);
                    
                    // Get questions for the quiz
                    List<Question> questions = questionDAO.findByQuiz(quiz);
                    
                    // Process student answers
                    int totalPoints = 0;
                    int earnedPoints = 0;
                    
                    for (Question question : questions) {
                        totalPoints += question.getPoints();
                        
                        // Get student's answer for this question
                        String answerParam = request.getParameter("question_" + question.getId());
                        
                        if (answerParam != null && !answerParam.trim().isEmpty()) {
                            StudentAnswer studentAnswer = new StudentAnswer();
                            studentAnswer.setSubmission(submission);
                            studentAnswer.setQuestion(question);
                            studentAnswer.setAnswerText(answerParam);
                            
                            // Determine if the answer is correct for multiple choice and true/false questions
                            if (question.getType() == Question.QuestionType.MULTIPLE_CHOICE || 
                                question.getType() == Question.QuestionType.TRUE_FALSE) {
                                
                                try {
                                    Long answerId = Long.parseLong(answerParam);
                                    Answer selectedAnswer = answerDAO.findById(answerId);
                                    
                                    if (selectedAnswer != null && selectedAnswer.getQuestion().getId().equals(question.getId())) {
                                        studentAnswer.setCorrect(selectedAnswer.isCorrect());
                                        
                                        if (selectedAnswer.isCorrect()) {
                                            earnedPoints += question.getPoints();
                                        }
                                    } else {
                                        studentAnswer.setCorrect(false);
                                    }
                                } catch (NumberFormatException e) {
                                    studentAnswer.setCorrect(false);
                                }
                            } else {
                                // For short answer and essay questions, correctness will be determined by the teacher
                                studentAnswer.setCorrect(null);
                            }
                            
                            submissionDAO.createStudentAnswer(studentAnswer);
                        }
                    }
                    
                    // Calculate score as a percentage
                    double score = totalPoints > 0 ? (double) earnedPoints / totalPoints * 100 : 0;
                    
                    // Create grade for automatically graded questions
                    Grade grade = new Grade();
                    grade.setStudent(student);
                    grade.setCourse(quiz.getCourse());
                    grade.setQuiz(quiz);
                    grade.setScore(score);
                    gradeDAO.create(grade);
                    
                    // Remove quiz start time from session
                    session.removeAttribute("quizStartTime");
                    
                    // Redirect to result page
                    response.sendRedirect(request.getContextPath() + "/student/quiz/result/" + submission.getId());
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}