package com.lms.dao;

import com.lms.model.Course;
import com.lms.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CourseDAO extends BaseDAO<Course> {
    
    public CourseDAO() {
        super(Course.class);
    }
    
    public List<Course> findByTeacher(User teacher) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.teacher = :teacher", Course.class);
            query.setParameter("teacher", teacher);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Course> findActiveCourses() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.active = true", Course.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Course> findCoursesByStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT e.course FROM Enrollment e WHERE e.student = :student", Course.class);
            query.setParameter("student", student);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Course findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Course.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Course course) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Course course) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(course);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Course course = em.find(Course.class, id);
            if (course != null) {
                em.remove(course);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Course> searchCourses(String keyword) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.active = true AND " +
                    "(LOWER(c.title) LIKE LOWER(:keyword) OR LOWER(c.description) LIKE LOWER(:keyword))", 
                    Course.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}