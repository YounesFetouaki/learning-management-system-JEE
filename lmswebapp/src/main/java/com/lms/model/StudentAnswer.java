package com.lms.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "student_answers")
public class StudentAnswer implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;
    
    @Column(name = "is_correct")
    private Boolean correct;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Submission getSubmission() {
        return submission;
    }
    
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public String getAnswerText() {
        return answerText;
    }
    
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    
    public Boolean getCorrect() {
        return correct;
    }
    
    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
    
    @Override
    public String toString() {
        return "StudentAnswer{" +
                "id=" + id +
                ", question=" + (question != null ? question.getText() : "null") +
                ", answerText='" + answerText + '\'' +
                ", correct=" + correct +
                '}';
    }
}