package com.lms.dao;

import com.lms.model.Answer;
import com.lms.model.Question;
import com.lms.model.Quiz;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class QuestionDAO extends BaseDAO<Question> {
    
    public QuestionDAO() {
        super(Question.class);
    }
    
    // Create or Update (save)
    public Question save(Question question) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Question savedQuestion = em.merge(question);  // merge works for both new and existing entities
            em.getTransaction().commit();
            return savedQuestion;
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
            Question question = em.find(Question.class, id);
            if (question != null) {
                em.remove(question);
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
    public Question findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Question.class, id);
        } finally {
            em.close();
        }
    }
    
    // Find all questions
    public List<Question> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q ORDER BY q.id", 
                    Question.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find all questions with pagination
    public List<Question> findAll(int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q ORDER BY q.id", 
                    Question.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by quiz
    public List<Question> findByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by quiz with pagination
    public List<Question> findByQuiz(Quiz quiz, int limit, int offset) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by type
    public List<Question> findByType(Question.QuestionType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.type = :type ORDER BY q.id", 
                    Question.class);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by quiz and type
    public List<Question> findByQuizAndType(Quiz quiz, Question.QuestionType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz AND q.type = :type ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search questions by text content
    public List<Question> searchByText(String searchText) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.text LIKE :searchText ORDER BY q.id", 
                    Question.class);
            query.setParameter("searchText", "%" + searchText + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Search questions by text content within a quiz
    public List<Question> searchByTextInQuiz(Quiz quiz, String searchText) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz AND q.text LIKE :searchText ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            query.setParameter("searchText", "%" + searchText + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by points range
    public List<Question> findByPointsRange(int minPoints, int maxPoints) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.points BETWEEN :minPoints AND :maxPoints ORDER BY q.points, q.id", 
                    Question.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("maxPoints", maxPoints);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Find questions by quiz and points range
    public List<Question> findByQuizAndPointsRange(Quiz quiz, int minPoints, int maxPoints) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz AND q.points BETWEEN :minPoints AND :maxPoints ORDER BY q.points, q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            query.setParameter("minPoints", minPoints);
            query.setParameter("maxPoints", maxPoints);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Count questions
    public long countAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(q) FROM Question q", 
                    Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count questions by quiz
    public long countByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(q) FROM Question q WHERE q.quiz = :quiz", 
                    Long.class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count questions by type
    public long countByType(Question.QuestionType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(q) FROM Question q WHERE q.type = :type", 
                    Long.class);
            query.setParameter("type", type);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Count questions by quiz and type
    public long countByQuizAndType(Quiz quiz, Question.QuestionType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(q) FROM Question q WHERE q.quiz = :quiz AND q.type = :type", 
                    Long.class);
            query.setParameter("quiz", quiz);
            query.setParameter("type", type);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Get total points for a quiz
    public int getTotalPointsByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT SUM(q.points) FROM Question q WHERE q.quiz = :quiz", 
                    Long.class);
            query.setParameter("quiz", quiz);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } finally {
            em.close();
        }
    }
    
    // Get average points for a quiz
    public double getAveragePointsByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(q.points) FROM Question q WHERE q.quiz = :quiz", 
                    Double.class);
            query.setParameter("quiz", quiz);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }
    
    // Get question statistics by quiz
    public Object[] getQuestionStatsByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT COUNT(q), SUM(q.points), AVG(q.points), MIN(q.points), MAX(q.points) " +
                    "FROM Question q WHERE q.quiz = :quiz", 
                    Object[].class);
            query.setParameter("quiz", quiz);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    // Answer-related operations
    
    // Save an answer
    public Answer saveAnswer(Answer answer) {
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
    
    // Delete an answer
    public void deleteAnswer(Long answerId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Answer answer = em.find(Answer.class, answerId);
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
    
    // Find answer by id
    public Answer findAnswerById(Long answerId) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Answer.class, answerId);
        } finally {
            em.close();
        }
    }
    
    // Find answers by question
    public List<Answer> findAnswersByQuestion(Question question) {
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
    
    // Count answers by question
    public long countAnswersByQuestion(Question question) {
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
    public long countCorrectAnswersByQuestion(Question question) {
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
    
    // Batch operations
    
    // Save multiple questions
    public List<Question> saveQuestions(List<Question> questions) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (Question question : questions) {
                em.merge(question);
            }
            em.getTransaction().commit();
            return questions;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    // Delete multiple questions by IDs
    public void deleteQuestionsByIds(List<Long> questionIds) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (Long id : questionIds) {
                Question question = em.find(Question.class, id);
                if (question != null) {
                    em.remove(question);
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
    
    // Delete all questions by quiz
    public void deleteAllQuestionsByQuiz(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz", 
                    Question.class);
            query.setParameter("quiz", quiz);
            List<Question> questions = query.getResultList();
            
            for (Question question : questions) {
                em.remove(question);
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
    
    // Copy questions from one quiz to another
    public List<Question> copyQuestionsToQuiz(Quiz sourceQuiz, Quiz targetQuiz) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            List<Question> sourceQuestions = findByQuiz(sourceQuiz);
            List<Question> copiedQuestions = new java.util.ArrayList<>();
            
            for (Question sourceQuestion : sourceQuestions) {
                Question copiedQuestion = new Question();
                copiedQuestion.setQuiz(targetQuiz);
                copiedQuestion.setText(sourceQuestion.getText());
                copiedQuestion.setType(sourceQuestion.getType());
                copiedQuestion.setPoints(sourceQuestion.getPoints());
                
                em.persist(copiedQuestion);
                copiedQuestions.add(copiedQuestion);
                
                // Copy answers
                List<Answer> sourceAnswers = findAnswersByQuestion(sourceQuestion);
                for (Answer sourceAnswer : sourceAnswers) {
                    Answer copiedAnswer = new Answer();
                    copiedAnswer.setQuestion(copiedQuestion);
                    copiedAnswer.setText(sourceAnswer.getText());
                    copiedAnswer.setCorrect(sourceAnswer.isCorrect());
                    em.persist(copiedAnswer);
                }
            }
            
            em.getTransaction().commit();
            return copiedQuestions;
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
    
    // Check if question has correct answers
    public boolean hasCorrectAnswers(Question question) {
        return countCorrectAnswersByQuestion(question) > 0;
    }
    
    // Validate question (has at least one answer and at least one correct answer for multiple choice)
    public boolean isQuestionValid(Question question) {
        long answerCount = countAnswersByQuestion(question);
        if (answerCount == 0) {
            return false;
        }
        
        if (question.getType() == Question.QuestionType.MULTIPLE_CHOICE || 
            question.getType() == Question.QuestionType.TRUE_FALSE) {
            return countCorrectAnswersByQuestion(question) > 0;
        }
        
        return true;
    }
    
    // Get questions that need review (no correct answers for MC/TF questions)
    public List<Question> getQuestionsNeedingReview(Quiz quiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.quiz = :quiz AND " +
                    "(q.type IN ('MULTIPLE_CHOICE', 'TRUE_FALSE') AND " +
                    "q.id NOT IN (SELECT a.question.id FROM Answer a WHERE a.correct = true)) " +
                    "ORDER BY q.id", 
                    Question.class);
            query.setParameter("quiz", quiz);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}