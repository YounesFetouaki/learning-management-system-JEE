package com.lms.controller;

import com.lms.dao.CalendarEventDAO;
import com.lms.dao.CourseDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.CalendarEvent;
import com.lms.model.Course;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/calendar/*")
public class CalendarServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final CalendarEventDAO calendarEventDAO = new CalendarEventDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Display calendar page
            request.getRequestDispatcher("/calendar.jsp").forward(request, response);
        } else if (pathInfo.equals("/events")) {
            // Return events as JSON for AJAX requests
            String startDateStr = request.getParameter("start");
            String endDateStr = request.getParameter("end");
            
            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);
                
                List<CalendarEvent> events = calendarEventDAO.findByDateRange(startDate, endDate, user);
                
                // Convert events to JSON
                JSONArray jsonEvents = new JSONArray();
                for (CalendarEvent event : events) {
                    JSONObject jsonEvent = new JSONObject();
                    jsonEvent.put("id", event.getId());
                    jsonEvent.put("title", event.getTitle());
                    jsonEvent.put("start", dateFormat.format(event.getStartDate()));
                    
                    if (event.getEndDate() != null) {
                        jsonEvent.put("end", dateFormat.format(event.getEndDate()));
                    }
                    
                    jsonEvent.put("allDay", event.isAllDay());
                    
                    if (event.getColor() != null) {
                        jsonEvent.put("color", event.getColor());
                    }
                    
                    // Add URL for clickable events
                    if (event.getEventType() == CalendarEvent.EventType.QUIZ_START || 
                        event.getEventType() == CalendarEvent.EventType.QUIZ_END) {
                        if (event.getQuiz() != null) {
                            if (user.getRole() == User.Role.STUDENT) {
                                jsonEvent.put("url", request.getContextPath() + "/student/quiz/take/" + event.getQuiz().getId());
                            } else if (user.getRole() == User.Role.TEACHER) {
                                jsonEvent.put("url", request.getContextPath() + "/teacher/quizzes/edit/" + event.getQuiz().getId());
                            }
                        }
                    } else if (event.getEventType() == CalendarEvent.EventType.COURSE_START || 
                               event.getEventType() == CalendarEvent.EventType.COURSE_END) {
                        if (event.getCourse() != null) {
                            if (user.getRole() == User.Role.STUDENT) {
                                jsonEvent.put("url", request.getContextPath() + "/student/courses/view/" + event.getCourse().getId());
                            } else if (user.getRole() == User.Role.TEACHER) {
                                jsonEvent.put("url", request.getContextPath() + "/teacher/courses/view/" + event.getCourse().getId());
                            }
                        }
                    }
                    
                    jsonEvents.put(jsonEvent);
                }
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonEvents.toString());
                out.flush();
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            }
        } else if (pathInfo.equals("/sync")) {
            // Sync calendar with courses and quizzes
            if (user.getRole() == User.Role.TEACHER) {
                List<Course> courses = courseDAO.findByTeacher(user);
                calendarEventDAO.generateEventsFromCourses(courses);
                
                for (Course course : courses) {
                    List<Quiz> quizzes = quizDAO.findByCourse(course);
                    calendarEventDAO.generateEventsFromQuizzes(quizzes);
                }
            } else if (user.getRole() == User.Role.STUDENT) {
                List<Course> courses = courseDAO.findCoursesByStudent(user);
                
                for (Course course : courses) {
                    List<Quiz> quizzes = quizDAO.findActiveQuizzesByCourse(course);
                    calendarEventDAO.generateEventsFromQuizzes(quizzes);
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/calendar");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Create a new personal event
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String allDayStr = request.getParameter("allDay");
            String color = request.getParameter("color");
            
            if (title == null || title.trim().isEmpty() || startDateStr == null || startDateStr.trim().isEmpty()) {
                request.setAttribute("error", "Event title and start date are required");
                request.getRequestDispatcher("/calendar.jsp").forward(request, response);
                return;
            }
            
            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = null;
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    endDate = dateFormat.parse(endDateStr);
                }
                
                CalendarEvent event = new CalendarEvent();
                event.setTitle(title);
                event.setDescription(description);
                event.setStartDate(startDate);
                event.setEndDate(endDate);
                event.setAllDay(allDayStr != null && allDayStr.equals("on"));
                event.setEventType(CalendarEvent.EventType.PERSONAL);
                event.setUser(user);
                
                if (color != null && !color.trim().isEmpty()) {
                    event.setColor(color);
                }
                
                calendarEventDAO.create(event);
                
                response.sendRedirect(request.getContextPath() + "/calendar");
            } catch (ParseException e) {
                request.setAttribute("error", "Invalid date format");
                request.getRequestDispatcher("/calendar.jsp").forward(request, response);
            }
        } else if (pathInfo.equals("/delete")) {
            // Delete an event
            String eventIdStr = request.getParameter("eventId");
            
            if (eventIdStr != null && !eventIdStr.trim().isEmpty()) {
                try {
                    Long eventId = Long.parseLong(eventIdStr);
                    CalendarEvent event = calendarEventDAO.findById(eventId);
                    
                    if (event != null && (event.getUser() == null || event.getUser().getId().equals(user.getId()))) {
                        calendarEventDAO.delete(event);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to delete this event");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid event ID");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID is required");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}