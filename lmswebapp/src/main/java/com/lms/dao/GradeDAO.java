package com.lms.dao;

import com.lms.model.Course;
import com.lms.model.Grade;
import com.lms.model.Quiz;
import com.lms.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class GradeDAO extends BaseDAO<Grade> {
    
    public GradeDAO() {
        super(Grade.class);
    }
    
    public Grade findByStudentAndQuiz(User student, Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Grade> query = em.createQuery(
                    "SELECT g FROM Grade g WHERE g.student = :student AND g.quiz = :quiz", 
                    Grade.class);
            query.setParameter("student", student);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    public List<Grade> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT g FROM Grade g ORDER BY g.createdAt DESC", Grade.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Grade grade = em.find(Grade.class, id);
            if (grade != null) {
                em.remove(grade);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Grade> findByStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Grade> query = em.createQuery(
                    "SELECT g FROM Grade g WHERE g.student = :student ORDER BY g.createdAt DESC", 
                    Grade.class);
            query.setParameter("student", student);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Grade> findByStudentAndCourse(User student, Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Grade> query = em.createQuery(
                    "SELECT g FROM Grade g WHERE g.student = :student AND g.course = :course ORDER BY g.createdAt DESC", 
                    Grade.class);
            query.setParameter("student", student);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Grade findById(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grade.class, id);
        } finally {
            em.close();
        }
    }

    public void create(Grade grade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(grade);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Grade grade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(grade);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public List<Grade> findByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Grade> query = em.createQuery(
                    "SELECT g FROM Grade g WHERE g.course = :course ORDER BY g.createdAt DESC", 
                    Grade.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Double calculateAverageGradeForStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(g.score) FROM Grade g WHERE g.student = :student", 
                    Double.class);
            query.setParameter("student", student);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }
    
    public Double calculateAverageGradeForCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(g.score) FROM Grade g WHERE g.course = :course", 
                    Double.class);
            query.setParameter("course", course);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Grade grade = em.find(Grade.class, id);
            if (grade != null) {
                em.remove(grade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}