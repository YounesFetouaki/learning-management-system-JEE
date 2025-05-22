<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Results - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>Quiz Results: ${submission.quiz.title}</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/student/courses/view/${submission.quiz.course.id}" class="btn btn-secondary">
                        <i class="icon-arrow-left"></i> Back to Course
                    </a>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h2>Summary</h2>
                </div>
                <div class="card-body">
                    <div class="quiz-result-summary">
                        <div class="result-item">
                            <span class="result-label">Submission Date:</span>
                            <span class="result-value"><fmt:formatDate value="${submission.submissionDate}" pattern="yyyy-MM-dd HH:mm" /></span>
                        </div>
                        <div class="result-item">
                            <span class="result-label">Course:</span>
                            <span class="result-value">${submission.quiz.course.title}</span>
                        </div>
                        <div class="result-item">
                            <span class="result-label">Quiz:</span>
                            <span class="result-value">${submission.quiz.title}</span>
                        </div>
                        <c:if test="${grade != null}">
                            <div class="result-item">
                                <span class="result-label">Score:</span>
                                <span class="result-value ${grade.score >= (submission.quiz.passingScore != null ? submission.quiz.passingScore : 70) ? 'passing-grade' : 'failing-grade'}">
                                    <fmt:formatNumber value="${grade.score}" maxFractionDigits="1" />%
                                </span>
                            </div>
                            <c:if test="${submission.quiz.passingScore != null}">
                                <div class="result-item">
                                    <span class="result-label">Status:</span>
                                    <span class="result-value ${grade.score >= submission.quiz.passingScore ? 'passing-grade' : 'failing-grade'}">
                                        ${grade.score >= submission.quiz.passingScore ? 'Passed' : 'Failed'}
                                    </span>
                                </div>
                            </c:if>
                            <c:if test="${not empty grade.feedback}">
                                <div class="result-item">
                                    <span class="result-label">Feedback:</span>
                                    <span class="result-value">${grade.feedback}</span>
                                </div>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h2>Your Answers</h2>
                </div>
                <div class="card-body">
                    <div class="quiz-answers">
                        <c:forEach items="${submission.studentAnswers}" var="studentAnswer" varStatus="status">
                            <div class="question-card">
                                <div class="question-header">
                                    <div class="question-number">Question ${status.index + 1}</div>
                                    <div class="question-points">${studentAnswer.question.points} point${studentAnswer.question.points != 1 ? 's' : ''}</div>
                                    <c:if test="${studentAnswer.correct != null}">
                                        <div class="question-result ${studentAnswer.correct ? 'correct' : 'incorrect'}">
                                            ${studentAnswer.correct ? 'Correct' : 'Incorrect'}
                                        </div>
                                    </c:if>
                                </div>
                                <div class="question-content">
                                    <p class="question-text">${studentAnswer.question.text}</p>
                                    
                                    <div class="student-answer">
                                        <h4>Your Answer:</h4>
                                        <p>${studentAnswer.answerText}</p>
                                    </div>
                                    
                                    <c:if test="${studentAnswer.correct != null && !studentAnswer.correct}">
                                        <div class="correct-answer">
                                            <h4>Correct Answer:</h4>
                                            <c:forEach items="${studentAnswer.question.answers}" var="answer">
                                                <c:if test="${answer.correct}">
                                                    <p>${answer.text}</p>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
</body>
</html>