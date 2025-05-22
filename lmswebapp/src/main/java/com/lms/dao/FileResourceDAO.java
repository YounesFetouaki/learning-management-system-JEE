package com.lms.dao;

import com.lms.model.Course;
import com.lms.model.FileResource;
import com.lms.model.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class FileResourceDAO extends BaseDAO<FileResource> {
    
    public FileResourceDAO() {
        super(FileResource.class);
    }
    
    // Create or Update (save)
    public FileResource save(FileResource fileResource) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            FileResource savedFile = em.merge(fileResource);
            em.getTransaction().commit();
            return savedFile;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
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
            FileResource fileResource = em.find(FileResource.class, id);
            if (fileResource != null) {
                em.remove(fileResource);
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
    
    // Find by id
    public FileResource findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FileResource.class, id);
        } finally {
            em.close();
        }
    }
    
    // Find all files
    public List<FileResource> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find all files with pagination
    public List<FileResource> findAll(int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by course
    public List<FileResource> findByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.course = :course ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("course", course);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by uploader
    public List<FileResource> findByUploader(User uploader) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.uploadedBy = :uploader ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("uploader", uploader);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by course and uploader
    public List<FileResource> findByCourseAndUploader(Course course, User uploader) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.course = :course AND f.uploadedBy = :uploader ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("course", course);
            query.setParameter("uploader", uploader);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by file type
    public List<FileResource> findByFileType(String fileType) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.fileType = :fileType ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("fileType", fileType);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by course and file type
    public List<FileResource> findByCourseAndFileType(Course course, String fileType) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.course = :course AND f.fileType = :fileType ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("course", course);
            query.setParameter("fileType", fileType);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by filename (partial match)
    public List<FileResource> findByFilename(String filename) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.fileName LIKE :filename ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("filename", "%" + filename + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by description (partial match)
    public List<FileResource> findByDescription(String description) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.description LIKE :description ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("description", "%" + description + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search files by keyword (searches filename and description)
    public List<FileResource> searchFiles(String keyword) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.fileName LIKE :keyword OR f.description LIKE :keyword ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search files by course and keyword
    public List<FileResource> searchFilesByCourse(Course course, String keyword) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.course = :course AND (f.fileName LIKE :keyword OR f.description LIKE :keyword) ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("course", course);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by date range
    public List<FileResource> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.uploadDate BETWEEN :startDate AND :endDate ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by size range
    public List<FileResource> findBySizeRange(long minSize, long maxSize) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.fileSize BETWEEN :minSize AND :maxSize ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("minSize", minSize);
            query.setParameter("maxSize", maxSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Count files
    public long countAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(f) FROM FileResource f", 
                    Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count files by course
    public long countByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(f) FROM FileResource f WHERE f.course = :course", 
                    Long.class);
            query.setParameter("course", course);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count files by uploader
    public long countByUploader(User uploader) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(f) FROM FileResource f WHERE f.uploadedBy = :uploader", 
                    Long.class);
            query.setParameter("uploader", uploader);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Get total file size by course
    public long getTotalFileSizeByCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT SUM(f.fileSize) FROM FileResource f WHERE f.course = :course", 
                    Long.class);
            query.setParameter("course", course);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } finally {
            em.close();
        }
    }
    
    // Get total file size by uploader
    public long getTotalFileSizeByUploader(User uploader) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT SUM(f.fileSize) FROM FileResource f WHERE f.uploadedBy = :uploader", 
                    Long.class);
            query.setParameter("uploader", uploader);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } finally {
            em.close();
        }
    }
    
    // Find recent files (for dashboard)
    public List<FileResource> findRecentFiles(int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find recent files by course
    public List<FileResource> findRecentFilesByCourse(Course course, int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.course = :course ORDER BY f.uploadDate DESC", 
                    FileResource.class);
            query.setParameter("course", course);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find files by file path (for checking duplicates)
    public FileResource findByFilePath(String filePath) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<FileResource> query = em.createQuery(
                    "SELECT f FROM FileResource f WHERE f.filePath = :filePath", 
                    FileResource.class);
            query.setParameter("filePath", filePath);
            List<FileResource> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    
    // Get file statistics
    public Object[] getFileStats() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT COUNT(f), SUM(f.fileSize), AVG(f.fileSize), " +
                    "MIN(f.uploadDate), MAX(f.uploadDate) " +
                    "FROM FileResource f", 
                    Object[].class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}