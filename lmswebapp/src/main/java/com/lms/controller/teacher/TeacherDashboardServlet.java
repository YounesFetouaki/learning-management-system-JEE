package com.lms.controller.teacher;

import com.lms.dao.CourseDAO;
import com.lms.dao.EnrollmentDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Quiz;
import com.lms.model.Submission;
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

@WebServlet("/teacher/dashboard")
public class TeacherDashboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final CourseDAO courseDAO = new CourseDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        
        // Get teacher's courses
        List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
        
        // Count statistics
        int courseCount = teacherCourses.size();
        int quizCount = 0;
        int studentCount = 0;
        int pendingGradesCount = 0;
        
        // Get all quizzes for all courses
        List<Quiz> allQuizzes = new ArrayList<>();
        for (Course course : teacherCourses) {
            List<Quiz> courseQuizzes = quizDAO.findByCourse(course);
            allQuizzes.addAll(courseQuizzes);
            quizCount += courseQuizzes.size();
            
            // Count students enrolled in this course
            studentCount += enrollmentDAO.countStudentsByCourse(course);
        }
        
        // Set attributes for the dashboard
        request.setAttribute("courseCount", courseCount);
        request.setAttribute("quizCount", quizCount);
        request.setAttribute("studentCount", studentCount);
        request.setAttribute("pendingGradesCount", pendingGradesCount);
        
        // Mock recent submissions data (in a real app, this would come from a database)
        List<Submission> recentSubmissions = new ArrayList<>();
        request.setAttribute("recentSubmissions", recentSubmissions);
        
        // Mock upcoming quizzes data
        List<Map<String, Object>> upcomingQuizzes = new ArrayList<>();
        
        // In a real application, you would query the database for upcoming quizzes
        // For now, we'll create some mock data
        if (!allQuizzes.isEmpty()) {
            for (int i = 0; i < Math.min(3, allQuizzes.size()); i++) {
                Quiz quiz = allQuizzes.get(i);
                Map<String, Object> quizData = new HashMap<>();
                quizData.put("course", quiz.getCourse());
                quizData.put("title", quiz.getTitle());
                quizData.put("endDate", quiz.getEndDate() != null ? quiz.getEndDate() : new Date());
                quizData.put("submissionCount", 5); // Mock data
                quizData.put("enrollmentCount", 10); // Mock data
                upcomingQuizzes.add(quizData);
            }
        }
        
        request.setAttribute("upcomingQuizzes", upcomingQuizzes);
        
        // Forward to the dashboard JSP
        request.getRequestDispatcher("/teacher/dashboard.jsp").forward(request, response);
    }
}