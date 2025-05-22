<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quizzes - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>Quizzes</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/teacher/quizzes/create" class="btn btn-primary">
                        <i class="icon-plus"></i> Create New Quiz
                    </a>
                </div>
            </div>
            
            <c:if test="${not empty success}">
                <div class="success-message">
                    <p>${success}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-header">
                    <h2>Select Course</h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/teacher/quizzes" method="get" class="form-inline">
                        <div class="form-group">
                            <label for="courseId">Course:</label>
                            <select id="courseId" name="courseId" class="form-control" onchange="this.form.submit()">
                                <option value="">Select a course</option>
                                <c:forEach items="${courses}" var="course">
                                    <option value="${course.id}" ${selectedCourse.id == course.id ? 'selected' : ''}>${course.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
            
            <c:if test="${not empty selectedCourse}">
                <div class="card mt-4">
                    <div class="card-header">
                        <h2>Quizzes for ${selectedCourse.title}</h2>
                        <div class="card-tools">
                            <input type="text" id="quizSearch" placeholder="Search quizzes..." class="search-input">
                        </div>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty quizzes}">
                                <div class="table-responsive">
                                    <table class="data-table" id="quizzesTable">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>Questions</th>
                                                <th>Time Limit</th>
                                                <th>Passing Score</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${quizzes}" var="quiz">
                                                <tr>
                                                    <td>${quiz.title}</td>
                                                    <td>${quiz.questions.size()}</td>
                                                    <td>${quiz.timeLimitMinutes != null ? quiz.timeLimitMinutes : 'No limit'} ${quiz.timeLimitMinutes != null ? 'min' : ''}</td>
                                                    <td>${quiz.passingScore != null ? quiz.passingScore : 'Not set'}${quiz.passingScore != null ? '%' : ''}</td>
                                                    <td>
                                                        <c:if test="${quiz.startDate != null}">
                                                            <fmt:formatDate value="${quiz.startDate}" pattern="yyyy-MM-dd HH:mm" />
                                                        </c:if>
                                                        <c:if test="${quiz.startDate == null}">
                                                            Not set
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${quiz.endDate != null}">
                                                            <fmt:formatDate value="${quiz.endDate}" pattern="yyyy-MM-dd HH:mm" />
                                                        </c:if>
                                                        <c:if test="${quiz.endDate == null}">
                                                            Not set
                                                        </c:if>
                                                    </td>
                                                    <td><span class="status-indicator ${quiz.active ? 'active' : 'inactive'}">${quiz.active ? 'Active' : 'Inactive'}</span></td>
                                                    <td class="actions">
                                                        <a href="${pageContext.request.contextPath}/teacher/questions/quiz/${quiz.id}" class="btn btn-sm btn-primary">Questions</a>
                                                        <a href="${pageContext.request.contextPath}/teacher/quizzes/edit/${quiz.id}" class="btn btn-sm btn-secondary">Edit</a>
                                                        <button class="btn btn-sm btn-danger" onclick="confirmDelete(${quiz.id}, '${quiz.title}')">Delete</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <p>No quizzes found for this course.</p>
                                    <a href="${pageContext.request.contextPath}/teacher/quizzes/create" class="btn btn-primary">Create a Quiz</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Deletion</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete the quiz <span id="quizTitle"></span>?</p>
                <p>This action cannot be undone and will remove all questions and student submissions for this quiz.</p>
            </div>
            <div class="modal-footer">
                <button id="cancelDelete" class="btn btn-secondary">Cancel</button>
                <form id="deleteForm" method="post">
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        // Search functionality
        document.getElementById('quizSearch').addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const table = document.getElementById('quizzesTable');
            const rows = table.getElementsByTagName('tr');
            
            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const cells = row.getElementsByTagName('td');
                let found = false;
                
                for (let j = 0; j < cells.length - 1; j++) {
                    const cellText = cells[j].textContent.toLowerCase();
                    if (cellText.indexOf(searchValue) > -1) {
                        found = true;
                        break;
                    }
                }
                
                row.style.display = found ? '' : 'none';
            }
        });
        
        // Delete confirmation modal
        const modal = document.getElementById('deleteModal');
        const closeBtn = document.getElementsByClassName('close')[0];
        const cancelBtn = document.getElementById('cancelDelete');
        
        function confirmDelete(quizId, title) {
            document.getElementById('quizTitle').textContent = title;
            document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/teacher/quizzes/delete/' + quizId;
            modal.style.display = 'block';
        }
        
        closeBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        cancelBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    </script>
</body>
</html>