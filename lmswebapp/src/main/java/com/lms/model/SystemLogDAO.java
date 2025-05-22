package com.lms.model;

import com.lms.dao.BaseDAO;
import com.lms.model.SystemLog;
import com.lms.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class SystemLogDAO extends BaseDAO<SystemLog> {
    
    public SystemLogDAO() {
        super(SystemLog.class);
    }
    
    /**
     * Find logs by category
     */
    public List<SystemLog> findByCategory(String category, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                    "SELECT l FROM SystemLog l WHERE l.category = :category ORDER BY l.timestamp DESC",
                    SystemLog.class);
            query.setParameter("category", category);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Find logs by user
     */
    public List<SystemLog> findByUser(User user, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                    "SELECT l FROM SystemLog l WHERE l.userId = :userId ORDER BY l.timestamp DESC",
                    SystemLog.class);
            query.setParameter("userId", user.getId());
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Find logs by date range
     */
    public List<SystemLog> findByDateRange(Date startDate, Date endDate, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                    "SELECT l FROM SystemLog l WHERE l.timestamp BETWEEN :startDate AND :endDate ORDER BY l.timestamp DESC",
                    SystemLog.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Find logs by level
     */
    public List<SystemLog> findByLevel(String level, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                    "SELECT l FROM SystemLog l WHERE l.level = :level ORDER BY l.timestamp DESC",
                    SystemLog.class);
            query.setParameter("level", level);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Search logs by keyword in message or details
     */
    public List<SystemLog> searchLogs(String keyword, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                    "SELECT l FROM SystemLog l WHERE l.message LIKE :keyword OR l.details LIKE :keyword ORDER BY l.timestamp DESC",
                    SystemLog.class);
            query.setParameter("keyword", "%" + keyword + "%");
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Count total logs
     */
    public long countLogs() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(l) FROM SystemLog l",
                    Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    /**
     * Count logs by category
     */
    public long countLogsByCategory(String category) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(l) FROM SystemLog l WHERE l.category = :category",
                    Long.class);
            query.setParameter("category", category);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}
}