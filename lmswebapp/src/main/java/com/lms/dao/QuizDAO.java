package com.lms.dao;

import com.lms.model.Course;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class QuizDAO extends BaseDAO<Quiz> {
    
    public QuizDAO() {
        super(Quiz.class);
    }
    // Create or Update (save)
    public Quiz save(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Quiz savedQuiz = em.merge(quiz);  // merge works for both new and existing entities
            em.getTransaction().commit();
            return savedQuiz;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Delete by id
    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Quiz quiz = em.find(Quiz.class, id);
            if (quiz != null) {
                em.remove(quiz);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Find all quizzes (optionally by course or all)
    public List<Quiz> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery("SELECT q FROM Quiz q", Quiz.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // You can also overload findAll by course if needed:
    public List<Quiz> findAllByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery("SELECT q FROM Quiz q WHERE q.course = :course", Quiz.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }}
    public List<Quiz> findByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery(
                    "SELECT q FROM Quiz q WHERE q.course = :course", Quiz.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Quiz findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Quiz.class, id);
        } finally {
            em.close();
        }
    }

    public List<Quiz> findActiveQuizzesByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery(
                    "SELECT q FROM Quiz q WHERE q.course = :course AND q.active = true", Quiz.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Quiz> findAvailableQuizzes(Course course, Date currentDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery(
                    "SELECT q FROM Quiz q WHERE q.course = :course AND q.active = true " +
                    "AND (q.startDate IS NULL OR q.startDate <= :currentDate) " +
                    "AND (q.endDate IS NULL OR q.endDate >= :currentDate)", 
                    Quiz.class);
            query.setParameter("course", course);
            query.setParameter("currentDate", currentDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Quiz> findQuizzesNotAttemptedByStudent(Course course, User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quiz> query = em.createQuery(
                    "SELECT q FROM Quiz q WHERE q.course = :course AND q.active = true " +
                    "AND q NOT IN (SELECT s.quiz FROM Submission s WHERE s.student = :student)", 
                    Quiz.class);
            query.setParameter("course", course);
            query.setParameter("student", student);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}