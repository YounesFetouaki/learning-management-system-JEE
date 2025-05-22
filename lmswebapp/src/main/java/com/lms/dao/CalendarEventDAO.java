package com.lms.dao;

import com.lms.model.CalendarEvent;
import com.lms.model.Course;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CalendarEventDAO extends BaseDAO<CalendarEvent> {
    
    public CalendarEventDAO() {
        super(CalendarEvent.class);
    }
    
    public List<CalendarEvent> findByUser(User user) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CalendarEvent> query = em.createQuery(
                    "SELECT e FROM CalendarEvent e WHERE e.user = :user ORDER BY e.startDate", 
                    CalendarEvent.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<CalendarEvent> findByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CalendarEvent> query = em.createQuery(
                    "SELECT e FROM CalendarEvent e WHERE e.course = :course ORDER BY e.startDate", 
                    CalendarEvent.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<CalendarEvent> findByDateRange(Date startDate, Date endDate, User user) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CalendarEvent> query = em.createQuery(
                    "SELECT e FROM CalendarEvent e WHERE " +
                    "((e.startDate BETWEEN :startDate AND :endDate) OR " +
                    "(e.endDate BETWEEN :startDate AND :endDate) OR " +
                    "(e.startDate <= :startDate AND e.endDate >= :endDate)) AND " +
                    "(e.user = :user OR e.user IS NULL) " +
                    "ORDER BY e.startDate", 
                    CalendarEvent.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<CalendarEvent> findUpcomingEvents(User user, int days) {
        EntityManager em = getEntityManager();
        try {
            Date now = new Date();
            // Calculate date X days from now
            Date futureDate = new Date(now.getTime() + (days * 24 * 60 * 60 * 1000L));
            
            TypedQuery<CalendarEvent> query = em.createQuery(
                    "SELECT e FROM CalendarEvent e WHERE " +
                    "e.startDate BETWEEN :now AND :futureDate AND " +
                    "(e.user = :user OR e.user IS NULL) " +
                    "ORDER BY e.startDate", 
                    CalendarEvent.class);
            query.setParameter("now", now);
            query.setParameter("futureDate", futureDate);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void generateEventsFromCourses(List<Course> courses) {
        for (Course course : courses) {
            // Create course start event
            if (course.getStartDate() != null) {
                CalendarEvent startEvent = new CalendarEvent();
                startEvent.setTitle("Course Start: " + course.getTitle());
                startEvent.setDescription("Start of the course: " + course.getTitle());
                startEvent.setStartDate(course.getStartDate());
                startEvent.setEndDate(course.getStartDate());
                startEvent.setAllDay(true);
                startEvent.setEventType(CalendarEvent.EventType.COURSE_START);
                startEvent.setCourse(course);
                startEvent.setColor("#4CAF50"); // Green
                create(startEvent);
            }
            
            // Create course end event
            if (course.getEndDate() != null) {
                CalendarEvent endEvent = new CalendarEvent();
                endEvent.setTitle("Course End: " + course.getTitle());
                endEvent.setDescription("End of the course: " + course.getTitle());
                endEvent.setStartDate(course.getEndDate());
                endEvent.setEndDate(course.getEndDate());
                endEvent.setAllDay(true);
                endEvent.setEventType(CalendarEvent.EventType.COURSE_END);
                endEvent.setCourse(course);
                endEvent.setColor("#F44336"); // Red
                create(endEvent);
            }
        }
    }
    
    public void generateEventsFromQuizzes(List<Quiz> quizzes) {
        for (Quiz quiz : quizzes) {
            // Create quiz start event
            if (quiz.getStartDate() != null) {
                CalendarEvent startEvent = new CalendarEvent();
                startEvent.setTitle("Quiz Start: " + quiz.getTitle());
                startEvent.setDescription("Start of the quiz: " + quiz.getTitle() + " for course: " + quiz.getCourse().getTitle());
                startEvent.setStartDate(quiz.getStartDate());
                startEvent.setEndDate(quiz.getStartDate());
                startEvent.setAllDay(true);
                startEvent.setEventType(CalendarEvent.EventType.QUIZ_START);
                startEvent.setCourse(quiz.getCourse());
                startEvent.setQuiz(quiz);
                startEvent.setColor("#2196F3"); // Blue
                create(startEvent);
            }
            
            // Create quiz end event
            if (quiz.getEndDate() != null) {
                CalendarEvent endEvent = new CalendarEvent();
                endEvent.setTitle("Quiz Due: " + quiz.getTitle());
                endEvent.setDescription("Due date for the quiz: " + quiz.getTitle() + " for course: " + quiz.getCourse().getTitle());
                endEvent.setStartDate(quiz.getEndDate());
                endEvent.setEndDate(quiz.getEndDate());
                endEvent.setAllDay(true);
                endEvent.setEventType(CalendarEvent.EventType.QUIZ_END);
                endEvent.setCourse(quiz.getCourse());
                endEvent.setQuiz(quiz);
                endEvent.setColor("#FF9800"); // Orange
                create(endEvent);
            }
        }
    }
}