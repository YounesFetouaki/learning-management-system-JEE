package com.lms.dao;

import com.lms.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UserDAO extends BaseDAO<User> {
    
    public UserDAO() {
        super(User.class);
    }
    
    public User findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
 // Add these methods to your existing UserDAO.java
    public List<User> findAllUsers() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u ORDER BY u.id", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public User saveOrUpdate(User user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User savedUser;
            if (user.getId() == null) {
                em.persist(user);
                savedUser = user;
            } else {
                savedUser = em.merge(user);
            }
            em.getTransaction().commit();
            return savedUser;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void toggleUserStatus(Long userId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setActive(!user.isActive());
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    public User findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public List<User> findByRole(User.Role role) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.role = :role", User.class);
            query.setParameter("role", role);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<User> findActiveUsers() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.active = true", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.isActive()) {
            // In a real application, you would use a password hashing library
            // like BCrypt to check the password
            return user.getPasswordHash().equals(password);
        }
        return false;
    }

    public List<User> findAllStudents() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.role = :role", User.class);
            query.setParameter("role", User.Role.STUDENT);  // Assuming your Role enum has STUDENT constant
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;  // optionally rethrow or handle it accordingly
        } finally {
            em.close();
        }
    }


}