<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty quiz ? 'Create' : 'Edit'} Quiz - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>${empty quiz ? 'Create New' : 'Edit'} Quiz</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/teacher/quizzes" class="btn btn-secondary">
                        <i class="icon-arrow-left"></i> Back to Quizzes
                    </a>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="error-message">
                    <p>${error}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/teacher/quizzes${empty quiz ? '' : '/edit/'.concat(quiz.id)}" method="post" class="form">
                        <div class="form-group">
                            <label for="title">Quiz Title</label>
                            <input type="text" id="title" name="title" value="${quiz.title}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea id="description" name="description" rows="3" required>${quiz.description}</textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="courseId">Course</label>
                            <select id="courseId" name="courseId" required>
                                <option value="">Select Course</option>
                                <c:forEach items="${courses}" var="course">
                                    <option value="${course.id}" ${quiz.course.id == course.id ? 'selected' : ''}>${course.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="timeLimit">Time Limit (minutes, optional)</label>
                                <input type="number" id="timeLimit" name="timeLimit" value="${quiz.timeLimitMinutes}" min="1">
                            </div>
                            <div class="form-group col-md-6">
                                <label for="passingScore">Passing Score (%, optional)</label>
                                <input type="number" id="passingScore" name="passingScore" value="${quiz.passingScore}" min="0" max="100">
                            </div>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="startDate">Start Date (optional)</label>
                                <input type="datetime-local" id="startDate" name="startDate" 
                                       value="<c:if test="${quiz.startDate != null}"><fmt:formatDate value="${quiz.startDate}" pattern="yyyy-MM-dd'T'HH:mm" /></c:if>">
                            </div>
                            <div class="form-group col-md-6">
                                <label for="endDate">End Date (optional)</label>
                                <input type="datetime-local" id="endDate" name="endDate" 
                                       value="<c:if test="${quiz.endDate != null}"><fmt:formatDate value="${quiz.endDate}" pattern="yyyy-MM-dd'T'HH:mm" /></c:if>">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="checkbox-container">
                                <input type="checkbox" id="active" name="active" ${empty quiz || quiz.active ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Active
                            </label>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">${empty quiz ? 'Create' : 'Update'} Quiz</button>
                            <a href="${pageContext.request.contextPath}/teacher/quizzes" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script>
        // Validate end date is after start date
        document.querySelector('form').addEventListener('submit', function(e) {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            
            if (startDate && endDate && new Date(startDate) >= new Date(endDate)) {
                e.preventDefault();
                alert('End date must be after start date');
            }
        });
    </script>
</body>
</html>