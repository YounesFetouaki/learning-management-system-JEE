package com.lms.dao;

import com.lms.model.Quiz;
import com.lms.model.StudentAnswer;
import com.lms.model.Submission;
import com.lms.model.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class SubmissionDAO extends BaseDAO<Submission> {

    public SubmissionDAO() {
        super(Submission.class);
    }

    // Create or Update (save)
    public Submission save(Submission submission) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Submission savedSubmission = em.merge(submission);
            em.getTransaction().commit();
            return savedSubmission;
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
            Submission submission = em.find(Submission.class, id);
            if (submission != null) {
                em.remove(submission);
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
    public Submission findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Submission.class, id);
        } finally {
            em.close();
        }
    }

    // Find all submissions
    public List<Submission> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s ORDER BY s.submissionDate DESC", 
                    Submission.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find all submissions with pagination
    public List<Submission> findAll(int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s ORDER BY s.submissionDate DESC", 
                    Submission.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find submissions by student
    public List<Submission> findByStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.student = :student ORDER BY s.submissionDate DESC", 
                    Submission.class);
            query.setParameter("student", student);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find submissions by quiz
    public List<Submission> findByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.quiz = :quiz ORDER BY s.submissionDate DESC", 
                    Submission.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find submission by student and quiz
    public Submission findByStudentAndQuiz(User student, Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.student = :student AND s.quiz = :quiz", 
                    Submission.class);
            query.setParameter("student", student);
            query.setParameter("quiz", quiz);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            em.close();
        }
    }

    // Find submissions by date range
    public List<Submission> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.submissionDate BETWEEN :startDate AND :endDate ORDER BY s.submissionDate DESC", 
                    Submission.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Count submissions
    public long countAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Submission s", 
                    Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Count submissions by student
    public long countByStudent(User student) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Submission s WHERE s.student = :student", 
                    Long.class);
            query.setParameter("student", student);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Count submissions by quiz
    public long countByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Submission s WHERE s.quiz = :quiz", 
                    Long.class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Count logs (for system monitoring)
    public long countLogs() {
        return countAll();
    }

    // Student Answer operations
    public StudentAnswer saveStudentAnswer(StudentAnswer studentAnswer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            StudentAnswer savedAnswer = em.merge(studentAnswer);
            em.getTransaction().commit();
            return savedAnswer;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void createStudentAnswer(StudentAnswer studentAnswer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(studentAnswer);
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

    public void updateStudentAnswer(StudentAnswer studentAnswer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(studentAnswer);
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

    public StudentAnswer findStudentAnswerById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StudentAnswer.class, id);
        } finally {
            em.close();
        }
    }

    public List<StudentAnswer> findStudentAnswersBySubmission(Submission submission) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<StudentAnswer> query = em.createQuery(
                    "SELECT sa FROM StudentAnswer sa WHERE sa.submission = :submission ORDER BY sa.id", 
                    StudentAnswer.class);
            query.setParameter("submission", submission);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find recent submissions (for dashboard)
    public List<Submission> findRecentSubmissions(int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s ORDER BY s.submissionDate DESC", 
                    Submission.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find submissions that need grading
    public List<Submission> findSubmissionsNeedingGrading() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Submission> query = em.createQuery(
                    "SELECT s FROM Submission s WHERE s.id NOT IN " +
                    "(SELECT g.submission.id FROM Grade g WHERE g.submission IS NOT NULL) " +
                    "ORDER BY s.submissionDate ASC", 
                    Submission.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Check if student has already submitted for a quiz
    public boolean hasStudentSubmitted(User student, Quiz quiz) {
        return findByStudentAndQuiz(student, quiz) != null;
    }

    // Get submission statistics
    public Object[] getSubmissionStats() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT COUNT(s), AVG(SIZE(s.studentAnswers)), " +
                    "MIN(s.submissionDate), MAX(s.submissionDate) " +
                    "FROM Submission s", 
                    Object[].class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}