<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${quiz.title} - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>${quiz.title}</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/student/courses/view/${quiz.course.id}" class="btn btn-secondary">
                        <i class="icon-arrow-left"></i> Back to Course
                    </a>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h2>Quiz Information</h2>
                </div>
                <div class="card-body">
                    <div class="quiz-info">
                        <div class="info-item">
                            <span class="info-label">Course:</span>
                            <span class="info-value">${quiz.course.title}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Description:</span>
                            <span class="info-value">${quiz.description}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Total Questions:</span>
                            <span class="info-value">${questions.size()}</span>
                        </div>
                        <c:if test="${quiz.timeLimitMinutes != null}">
                            <div class="info-item">
                                <span class="info-label">Time Limit:</span>
                                <span class="info-value">${quiz.timeLimitMinutes} minutes</span>
                            </div>
                        </c:if>
                        <c:if test="${quiz.passingScore != null}">
                            <div class="info-item">
                                <span class="info-label">Passing Score:</span>
                                <span class="info-value">${quiz.passingScore}%</span>
                            </div>
                        </c:if>
                        <c:if test="${quiz.endDate != null}">
                            <div class="info-item">
                                <span class="info-label">Due Date:</span>
                                <span class="info-value"><fmt:formatDate value="${quiz.endDate}" pattern="yyyy-MM-dd HH:mm" /></span>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <c:if test="${quiz.timeLimitMinutes != null}">
                <div class="quiz-timer" id="quizTimer">
                    Time Remaining: <span id="timer">${quiz.timeLimitMinutes}:00</span>
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/student/quiz/submit/${quiz.id}" method="post" id="quizForm">
                <div class="quiz-questions">
                    <c:forEach items="${questions}" var="question" varStatus="status">
                        <div class="question-card">
                            <div class="question-header">
                                <div class="question-number">Question ${status.index + 1}</div>
                                <div class="question-points">${question.points} point${question.points != 1 ? 's' : ''}</div>
                            </div>
                            <div class="question-content">
                                <p class="question-text">${question.text}</p>
                                
                                <c:choose>
                                    <c:when test="${question.type == 'MULTIPLE_CHOICE'}">
                                        <div class="answer-options">
                                            <c:forEach items="${questionAnswers[question.id]}" var="answer">
                                                <div class="answer-option">
                                                    <label class="radio-container">
                                                        <input type="radio" name="question_${question.id}" value="${answer.id}" required>
                                                        <span class="radio-checkmark"></span>
                                                        ${answer.text}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:when test="${question.type == 'TRUE_FALSE'}">
                                        <div class="answer-options">
                                            <c:forEach items="${questionAnswers[question.id]}" var="answer">
                                                <div class="answer-option">
                                                    <label class="radio-container">
                                                        <input type="radio" name="question_${question.id}" value="${answer.id}" required>
                                                        <span class="radio-checkmark"></span>
                                                        ${answer.text}
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:when test="${question.type == 'SHORT_ANSWER'}">
                                        <div class="answer-input">
                                            <input type="text" name="question_${question.id}" placeholder="Your answer" required>
                                        </div>
                                    </c:when>
                                    <c:when test="${question.type == 'ESSAY'}">
                                        <div class="answer-input">
                                            <textarea name="question_${question.id}" rows="5" placeholder="Your answer" required></textarea>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary" id="submitBtn">Submit Quiz</button>
                    <button type="button" class="btn btn-secondary" onclick="confirmCancel()">Cancel</button>
                </div>
            </form>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <!-- Confirmation Modal -->
    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Submission</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to submit your quiz?</p>
                <p>Once submitted, you cannot change your answers.</p>
            </div>
            <div class="modal-footer">
                <button id="cancelSubmit" class="btn btn-secondary">Cancel</button>
                <button id="confirmSubmit" class="btn btn-primary">Submit Quiz</button>
            </div>
        </div>
    </div>
    
    <!-- Cancel Confirmation Modal -->
    <div id="cancelModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Cancel</h3>
                <span class="close cancel-close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to cancel this quiz?</p>
                <p>Your progress will be lost.</p>
            </div>
            <div class="modal-footer">
                <button id="cancelCancel" class="btn btn-secondary">No, Continue Quiz</button>
                <a href="${pageContext.request.contextPath}/student/courses/view/${quiz.course.id}" class="btn btn-danger">Yes, Cancel Quiz</a>
            </div>
        </div>
    </div>
    
    <script>
        // Timer functionality
        <c:if test="${quiz.timeLimitMinutes != null}">
            let timeLeft = ${quiz.timeLimitMinutes} * 60; // Convert to seconds
            const timerElement = document.getElementById('timer');
            
            function updateTimer() {
                const minutes = Math.floor(timeLeft / 60);
                const seconds = timeLeft % 60;
                
                timerElement.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
                
                if (timeLeft <= 300) { // 5 minutes or less
                    document.getElementById('quizTimer').classList.add('timer-warning');
                }
                
                if (timeLeft <= 60) { // 1 minute or less
                    document.getElementById('quizTimer').classList.add('timer-danger');
                }
                
                if (timeLeft <= 0) {
                    clearInterval(timerInterval);
                    alert('Time is up! Your quiz will be submitted automatically.');
                    document.getElementById('quizForm').submit();
                }
                
                timeLeft--;
            }
            
            const timerInterval = setInterval(updateTimer, 1000);
            updateTimer(); // Initial call
        </c:if>
        
        // Form submission confirmation
        document.getElementById('quizForm').addEventListener('submit', function(e) {
            e.preventDefault();
            document.getElementById('confirmModal').style.display = 'block';
        });
        
        document.getElementById('confirmSubmit').addEventListener('click', function() {
            document.getElementById('quizForm').submit();
        });
        
        document.getElementById('cancelSubmit').addEventListener('click', function() {
            document.getElementById('confirmModal').style.display = 'none';
        });
        
        document.getElementsByClassName('close')[0].addEventListener('click', function() {
            document.getElementById('confirmModal').style.display = 'none';
        });
        
        // Cancel confirmation
        function confirmCancel() {
            document.getElementById('cancelModal').style.display = 'block';
        }
        
        document.getElementById('cancelCancel').addEventListener('click', function() {
            document.getElementById('cancelModal').style.display = 'none';
        });
        
        document.getElementsByClassName('cancel-close')[0].addEventListener('click', function() {
            document.getElementById('cancelModal').style.display = 'none';
        });
        
        // Close modals when clicking outside
        window.addEventListener('click', function(event) {
            if (event.target == document.getElementById('confirmModal')) {
                document.getElementById('confirmModal').style.display = 'none';
            }
            if (event.target == document.getElementById('cancelModal')) {
                document.getElementById('cancelModal').style.display = 'none';
            }
        });
        
        // Warn before leaving page
        window.addEventListener('beforeunload', function(e) {
            const confirmationMessage = 'If you leave this page, your quiz progress will be lost.';
            e.returnValue = confirmationMessage;
            return confirmationMessage;
        });
    </script>
</body>
</html>