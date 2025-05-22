package com.lms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "submissions")
public class Submission implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submission_date", nullable = false)
    private Date submissionDate;
    
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentAnswer> studentAnswers = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        if (submissionDate == null) {
            submissionDate = new Date();
        }
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    public Date getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public Set<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }
    
    public void setStudentAnswers(Set<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
    
    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", quiz=" + (quiz != null ? quiz.getTitle() : "null") +
                ", submissionDate=" + submissionDate +
                '}';
    }
}