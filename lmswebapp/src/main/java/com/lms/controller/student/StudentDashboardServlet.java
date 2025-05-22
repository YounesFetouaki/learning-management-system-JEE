package com.lms.controller.student;

import com.lms.dao.CourseDAO;
import com.lms.dao.GradeDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Grade;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.io.IOException;
import java.util.ArrayList;
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

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final CourseDAO courseDAO = new CourseDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");
        
        // Get student's enrolled courses
        List<Course> enrolledCourses = courseDAO.findCoursesByStudent(student);
        int enrolledCoursesCount = enrolledCourses.size();
        
        // Count completed and pending quizzes
        int completedQuizzesCount = 0;
        int pendingQuizzesCount = 0;
        List<Quiz> upcomingQuizzes = new ArrayList<>();
        Date currentDate = new Date();
        
        for (Course course : enrolledCourses) {
            // Get available quizzes for this course
            List<Quiz> availableQuizzes = quizDAO.findAvailableQuizzes(course, currentDate);
            
            // Get quizzes not attempted by the student
            List<Quiz> notAttemptedQuizzes = quizDAO.findQuizzesNotAttemptedByStudent(course, student);
            
            pendingQuizzesCount += notAttemptedQuizzes.size();
            
            // Add upcoming quizzes to the list (limit to 5)
            for (Quiz quiz : notAttemptedQuizzes) {
                if (upcomingQuizzes.size() < 5) {
                    upcomingQuizzes.add(quiz);
                }
            }
        }
        
        // Calculate average grade
        Double averageGrade = gradeDAO.calculateAverageGradeForStudent(student);
        
        // Get recent grades
        List<Grade> recentGrades = gradeDAO.findByStudent(student);
        if (recentGrades.size() > 5) {
            recentGrades = recentGrades.subList(0, 5);
        }
        
        // Set attributes for the dashboard
        request.setAttribute("enrolledCoursesCount", enrolledCoursesCount);
        request.setAttribute("completedQuizzesCount", completedQuizzesCount);
        request.setAttribute("pendingQuizzesCount", pendingQuizzesCount);
        request.setAttribute("averageGrade", String.format("%.1f", averageGrade));
        request.setAttribute("upcomingQuizzes", upcomingQuizzes);
        request.setAttribute("recentGrades", recentGrades);
        
        // Mock course announcements data
        List<Map<String, Object>> courseAnnouncements = new ArrayList<>();
        
        // In a real application, you would query the database for course announcements
        // For now, we'll create some mock data
        if (!enrolledCourses.isEmpty()) {
            for (int i = 0; i < Math.min(3, enrolledCourses.size()); i++) {
                Course course = enrolledCourses.get(i);
                Map<String, Object> announcement = new HashMap<>();
                announcement.put("course", course);
                announcement.put("createdAt", new Date());
                announcement.put("content", "Welcome to " + course.getTitle() + "! Please review the course materials and upcoming quiz deadlines.");
                courseAnnouncements.add(announcement);
            }
        }
        
        request.setAttribute("courseAnnouncements", courseAnnouncements);
        
        // Forward to the dashboard JSP
        request.getRequestDispatcher("/student/dashboard.jsp").forward(request, response);
    }
}