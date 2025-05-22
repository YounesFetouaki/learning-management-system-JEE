package com.lms.controller.teacher;

import com.lms.dao.CourseDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/teacher/quizzes/*")
public class QuizManagementServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final QuizDAO quizDAO = new QuizDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all quizzes for teacher's courses
            List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
            request.setAttribute("courses", teacherCourses);
            
            // Get course ID from request parameter if provided
            String courseIdParam = request.getParameter("courseId");
            if (courseIdParam != null && !courseIdParam.isEmpty()) {
                try {
                    Long courseId = Long.parseLong(courseIdParam);
                    Course selectedCourse = null;
                    
                    // Verify the course belongs to the teacher
                    for (Course course : teacherCourses) {
                        if (course.getId().equals(courseId)) {
                            selectedCourse = course;
                            break;
                        }
                    }
                    
                    if (selectedCourse != null) {
                        List<Quiz> quizzes = quizDAO.findByCourse(selectedCourse);
                        request.setAttribute("selectedCourse", selectedCourse);
                        request.setAttribute("quizzes", quizzes);
                    }
                } catch (NumberFormatException e) {
                    // Invalid course ID, ignore
                }
            }
            
            request.getRequestDispatcher("/teacher/quizzes.jsp").forward(request, response);
        } else if (pathInfo.equals("/create")) {
            // Show quiz creation form
            List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
            request.setAttribute("courses", teacherCourses);
            request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show quiz edit form
            try {
                Long quizId = Long.parseLong(pathInfo.substring(6));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null && quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                    request.setAttribute("courses", teacherCourses);
                    request.setAttribute("quiz", quiz);
                    request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit this quiz");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
            }
        } else if (pathInfo.startsWith("/questions/")) {
            // Redirect to question management
            response.sendRedirect(request.getContextPath() + "/teacher/questions" + pathInfo.substring(10));
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
            // Handle quiz creation
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String courseIdStr = request.getParameter("courseId");
            String timeLimitStr = request.getParameter("timeLimit");
            String passingScoreStr = request.getParameter("passingScore");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String activeStr = request.getParameter("active");
            
            // Validate input
            if (title == null || title.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                courseIdStr == null || courseIdStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Title, description, and course are required");
                List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                request.setAttribute("courses", teacherCourses);
                request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                return;
            }
            
            try {
                Long courseId = Long.parseLong(courseIdStr);
                Course course = courseDAO.findById(courseId);
                
                // Verify the course belongs to the teacher
                if (course == null || !course.getTeacher().getId().equals(teacher.getId())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to create a quiz for this course");
                    return;
                }
                
                // Create new quiz
                Quiz quiz = new Quiz();
                quiz.setTitle(title);
                quiz.setDescription(description);
                quiz.setCourse(course);
                
                // Set optional fields
                if (timeLimitStr != null && !timeLimitStr.trim().isEmpty()) {
                    quiz.setTimeLimitMinutes(Integer.parseInt(timeLimitStr));
                }
                
                if (passingScoreStr != null && !passingScoreStr.trim().isEmpty()) {
                    quiz.setPassingScore(Integer.parseInt(passingScoreStr));
                }
                
                if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                    try {
                        quiz.setStartDate(dateFormat.parse(startDateStr));
                    } catch (ParseException e) {
                        request.setAttribute("error", "Invalid start date format");
                        List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                        request.setAttribute("courses", teacherCourses);
                        request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                        return;
                    }
                }
                
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    try {
                        quiz.setEndDate(dateFormat.parse(endDateStr));
                    } catch (ParseException e) {
                        request.setAttribute("error", "Invalid end date format");
                        List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                        request.setAttribute("courses", teacherCourses);
                        request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                        return;
                    }
                }
                
                quiz.setActive(activeStr != null && activeStr.equals("on"));
                
                quizDAO.create(quiz);
                
                // Redirect to question management for the new quiz
                response.sendRedirect(request.getContextPath() + "/teacher/questions/quiz/" + quiz.getId());
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid course ID, time limit, or passing score");
                List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                request.setAttribute("courses", teacherCourses);
                request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
            }
        } else if (pathInfo.startsWith("/edit/")) {
            // Handle quiz update
            try {
                Long quizId = Long.parseLong(pathInfo.substring(6));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null && quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    String title = request.getParameter("title");
                    String description = request.getParameter("description");
                    String courseIdStr = request.getParameter("courseId");
                    String timeLimitStr = request.getParameter("timeLimit");
                    String passingScoreStr = request.getParameter("passingScore");
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    String activeStr = request.getParameter("active");
                    
                    // Validate input
                    if (title == null || title.trim().isEmpty() ||
                        description == null || description.trim().isEmpty() ||
                        courseIdStr == null || courseIdStr.trim().isEmpty()) {
                        
                        request.setAttribute("error", "Title, description, and course are required");
                        List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                        request.setAttribute("courses", teacherCourses);
                        request.setAttribute("quiz", quiz);
                        request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                        return;
                    }
                    
                    Long courseId = Long.parseLong(courseIdStr);
                    Course course = courseDAO.findById(courseId);
                    
                    // Verify the course belongs to the teacher
                    if (course == null || !course.getTeacher().getId().equals(teacher.getId())) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to assign this quiz to this course");
                        return;
                    }
                    
                    // Update quiz
                    quiz.setTitle(title);
                    quiz.setDescription(description);
                    quiz.setCourse(course);
                    
                    // Set optional fields
                    if (timeLimitStr != null && !timeLimitStr.trim().isEmpty()) {
                        quiz.setTimeLimitMinutes(Integer.parseInt(timeLimitStr));
                    } else {
                        quiz.setTimeLimitMinutes(null);
                    }
                    
                    if (passingScoreStr != null && !passingScoreStr.trim().isEmpty()) {
                        quiz.setPassingScore(Integer.parseInt(passingScoreStr));
                    } else {
                        quiz.setPassingScore(null);
                    }
                    
                    if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                        try {
                            quiz.setStartDate(dateFormat.parse(startDateStr));
                        } catch (ParseException e) {
                            request.setAttribute("error", "Invalid start date format");
                            List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                            request.setAttribute("courses", teacherCourses);
                            request.setAttribute("quiz", quiz);
                            request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        quiz.setStartDate(null);
                    }
                    
                    if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                        try {
                            quiz.setEndDate(dateFormat.parse(endDateStr));
                        } catch (ParseException e) {
                            request.setAttribute("error", "Invalid end date format");
                            List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
                            request.setAttribute("courses", teacherCourses);
                            request.setAttribute("quiz", quiz);
                            request.getRequestDispatcher("/teacher/quiz-form.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        quiz.setEndDate(null);
                    }
                    
                    quiz.setActive(activeStr != null && activeStr.equals("on"));
                    
                    quizDAO.update(quiz);
                    
                    response.sendRedirect(request.getContextPath() + "/teacher/quizzes?courseId=" + course.getId());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit this quiz");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID, course ID, time limit, or passing score");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Handle quiz deletion
            try {
                Long quizId = Long.parseLong(pathInfo.substring(8));
                Quiz quiz = quizDAO.findById(quizId);
                
                if (quiz != null && quiz.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    Long courseId = quiz.getCourse().getId();
                    quizDAO.delete(quiz);
                    response.sendRedirect(request.getContextPath() + "/teacher/quizzes?courseId=" + courseId);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to delete this quiz");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}