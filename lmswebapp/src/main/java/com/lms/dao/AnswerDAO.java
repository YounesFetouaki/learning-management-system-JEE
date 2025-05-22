package com.lms.dao;

import com.lms.model.Answer;
import com.lms.model.Question;
import com.lms.model.Quiz;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class AnswerDAO extends BaseDAO<Answer> {
    
    public AnswerDAO() {
        super(Answer.class);
    }
    
    // Create or Update (save)
    public Answer save(Answer answer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Answer savedAnswer = em.merge(answer);
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
    
    // Delete by id
    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Answer answer = em.find(Answer.class, id);
            if (answer != null) {
                em.remove(answer);
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
    public Answer findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Answer.class, id);
        } finally {
            em.close();
        }
    }
    
    // Find all answers
    public List<Answer> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a ORDER BY a.id", 
                    Answer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find all answers with pagination
    public List<Answer> findAll(int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a ORDER BY a.id", 
                    Answer.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find answers by question
    public List<Answer> findByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question ORDER BY a.id", 
                    Answer.class);
            query.setParameter("question", question);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find answers by question with pagination
    public List<Answer> findByQuestion(Question question, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question ORDER BY a.id", 
                    Answer.class);
            query.setParameter("question", question);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find correct answers by question
    public List<Answer> findCorrectAnswersByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question AND a.correct = true ORDER BY a.id", 
                    Answer.class);
            query.setParameter("question", question);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find incorrect answers by question
    public List<Answer> findIncorrectAnswersByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question AND a.correct = false ORDER BY a.id", 
                    Answer.class);
            query.setParameter("question", question);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find answers by quiz (across all questions in the quiz)
    public List<Answer> findByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question.quiz = :quiz ORDER BY a.question.id, a.id", 
                    Answer.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find correct answers by quiz
    public List<Answer> findCorrectAnswersByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question.quiz = :quiz AND a.correct = true ORDER BY a.question.id, a.id", 
                    Answer.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search answers by text content
    public List<Answer> searchByText(String searchText) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.text LIKE :searchText ORDER BY a.id", 
                    Answer.class);
            query.setParameter("searchText", "%" + searchText + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search answers by text content within a question
    public List<Answer> searchByTextInQuestion(Question question, String searchText) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question AND a.text LIKE :searchText ORDER BY a.id", 
                    Answer.class);
            query.setParameter("question", question);
            query.setParameter("searchText", "%" + searchText + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Count answers
    public long countAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a", 
                    Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count answers by question
    public long countByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a WHERE a.question = :question", 
                    Long.class);
            query.setParameter("question", question);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count correct answers by question
    public long countCorrectByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a WHERE a.question = :question AND a.correct = true", 
                    Long.class);
            query.setParameter("question", question);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count incorrect answers by question
    public long countIncorrectByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a WHERE a.question = :question AND a.correct = false", 
                    Long.class);
            query.setParameter("question", question);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count answers by quiz
    public long countByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a WHERE a.question.quiz = :quiz", 
                    Long.class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count correct answers by quiz
    public long countCorrectByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Answer a WHERE a.question.quiz = :quiz AND a.correct = true", 
                    Long.class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Batch operations
    
    // Save multiple answers
    public List<Answer> saveAnswers(List<Answer> answers) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (Answer answer : answers) {
                em.merge(answer);
            }
            em.getTransaction().commit();
            return answers;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    // Delete multiple answers by IDs
    public void deleteAnswersByIds(List<Long> answerIds) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (Long id : answerIds) {
                Answer answer = em.find(Answer.class, id);
                if (answer != null) {
                    em.remove(answer);
                }
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
    
    // Delete all answers by question
    public void deleteAllAnswersByQuestion(Question question) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question", 
                    Answer.class);
            query.setParameter("question", question);
            List<Answer> answers = query.getResultList();
            
            for (Answer answer : answers) {
                em.remove(answer);
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
    
    // Delete all answers by quiz
    public void deleteAllAnswersByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question.quiz = :quiz", 
                    Answer.class);
            query.setParameter("quiz", quiz);
            List<Answer> answers = query.getResultList();
            
            for (Answer answer : answers) {
                em.remove(answer);
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
    
    // Update correctness of answers for a question
    public void updateAnswerCorrectness(Question question, List<Long> correctAnswerIds) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            // First, set all answers for this question to incorrect
            TypedQuery<Answer> query = em.createQuery(
                    "SELECT a FROM Answer a WHERE a.question = :question", 
                    Answer.class);
            query.setParameter("question", question);
            List<Answer> allAnswers = query.getResultList();
            
            for (Answer answer : allAnswers) {
                answer.setCorrect(false);
                em.merge(answer);
            }
            
            // Then, set the specified answers to correct
            for (Long id : correctAnswerIds) {
                Answer answer = em.find(Answer.class, id);
                if (answer != null && answer.getQuestion().getId().equals(question.getId())) {
                    answer.setCorrect(true);
                    em.merge(answer);
                }
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
    
    // Utility methods
    
    // Check if a question has at least one correct answer
    public boolean hasCorrectAnswer(Question question) {
        return countCorrectByQuestion(question) > 0;
    }
    
    // Check if a specific answer is correct
    public boolean isAnswerCorrect(Long answerId) {
        EntityManager em = getEntityManager();
        try {
            Answer answer = em.find(Answer.class, answerId);
            return answer != null && answer.isCorrect();
        } finally {
            em.close();
        }
    }
    
    // Get the percentage of correct answers for a question
    public double getCorrectAnswerPercentage(Question question) {
        long total = countByQuestion(question);
        if (total == 0) {
            return 0.0;
        }
        
        long correct = countCorrectByQuestion(question);
        return (double) correct / total * 100.0;
    }
    
    // Get questions with no correct answers
    public List<Question> getQuestionsWithNoCorrectAnswers(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz AND " +
                    "q.id NOT IN (SELECT a.question.id FROM Answer a WHERE a.correct = true) " +
                    "ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Get questions with too many correct answers (more than 1 for true/false)
    public List<Question> getQuestionsWithTooManyCorrectAnswers(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT q, COUNT(a) FROM Question q JOIN Answer a ON a.question = q " +
                    "WHERE q.quiz = :quiz AND a.correct = true " +
                    "GROUP BY q HAVING COUNT(a) > CASE WHEN q.type = 'TRUE_FALSE' THEN 1 ELSE 1 END " +
                    "ORDER BY q.id", 
                    Object[].class);
            query.setParameter("quiz", quiz);
            
            List<Object[]> results = query.getResultList();
            List<Question> questions = new java.util.ArrayList<>();
            
            for (Object[] result : results) {
                questions.add((Question) result[0]);
            }
            
            return questions;
        } finally {
            em.close();
        }
    }
    
    // Copy answers from one question to another
    public List<Answer> copyAnswersToQuestion(Question sourceQuestion, Question targetQuestion) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            List<Answer> sourceAnswers = findByQuestion(sourceQuestion);
            List<Answer> copiedAnswers = new java.util.ArrayList<>();
            
            for (Answer sourceAnswer : sourceAnswers) {
                Answer copiedAnswer = new Answer();
                copiedAnswer.setQuestion(targetQuestion);
                copiedAnswer.setText(sourceAnswer.getText());
                copiedAnswer.setCorrect(sourceAnswer.isCorrect());
                
                em.persist(copiedAnswer);
                copiedAnswers.add(copiedAnswer);
            }
            
            em.getTransaction().commit();
            return copiedAnswers;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    // Shuffle answers for a question (returns shuffled list without modifying database)
    public List<Answer> getShuffledAnswers(Question question) {
        List<Answer> answers = findByQuestion(question);
        java.util.Collections.shuffle(answers);
        return answers;
    }
    
    // Get answer statistics for a quiz
    public Object[] getAnswerStatsByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT COUNT(a), " +
                    "SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END), " +
                    "AVG(CASE WHEN a.correct = true THEN 1 ELSE 0 END) " +
                    "FROM Answer a WHERE a.question.quiz = :quiz", 
                    Object[].class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}