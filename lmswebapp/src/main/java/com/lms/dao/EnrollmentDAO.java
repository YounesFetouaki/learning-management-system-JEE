package com.lms.dao;

import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class EnrollmentDAO extends BaseDAO<Enrollment> {
    
    public EnrollmentDAO() {
        super(Enrollment.class);
    }
    
    public Enrollment findByStudentAndCourse(User student, Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Enrollment> query = em.createQuery(
                    "SELECT e FROM Enrollment e WHERE e.student = :student AND e.course = :course", 
                    Enrollment.class);
            query.setParameter("student", student);
            query.setParameter("course", course);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<User> findStudentsByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT e.student FROM Enrollment e WHERE e.course = :course", 
                    User.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Course> findCoursesByStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT e.course FROM Enrollment e WHERE e.student = :student", 
                    Course.class);
            query.setParameter("student", student);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public int countStudentsByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course", 
                    Long.class);
            query.setParameter("course", course);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}