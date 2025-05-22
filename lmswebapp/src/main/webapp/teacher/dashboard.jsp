<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="dashboard">
            <h1>Teacher Dashboard</h1>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <h3>My Courses</h3>
                    <p class="stat-number">${courseCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Active Students</h3>
                    <p class="stat-number">${studentCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Quizzes</h3>
                    <p class="stat-number">${quizCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Pending Grades</h3>
                    <p class="stat-number">${pendingGradesCount}</p>
                </div>
            </div>
            
            <div class="dashboard-actions">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/teacher/courses" class="btn btn-primary">Manage Courses</a>
                    <a href="${pageContext.request.contextPath}/teacher/quizzes" class="btn btn-primary">Manage Quizzes</a>
                    <a href="${pageContext.request.contextPath}/teacher/grades" class="btn btn-primary">Manage Grades</a>
                    <a href="${pageContext.request.contextPath}/teacher/courses/create" class="btn btn-secondary">Create New Course</a>
                </div>
            </div>
            
            <div class="recent-activity">
                <h2>Recent Submissions</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Student</th>
                            <th>Course</th>
                            <th>Quiz</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentSubmissions}" var="submission">
                            <tr>
                                <td>${submission.submissionDate}</td>
                                <td>${submission.student.fullName}</td>
                                <td>${submission.quiz.course.title}</td>
                                <td>${submission.quiz.title}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher/grades/grade?submissionId=${submission.id}" class="btn btn-secondary">Grade</a>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty recentSubmissions}">
                            <tr>
                                <td colspan="5">No recent submissions</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="upcoming-events">
                <h2>Upcoming Quiz Deadlines</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Quiz</th>
                            <th>End Date</th>
                            <th>Submissions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${upcomingQuizzes}" var="quiz">
                            <tr>
                                <td>${quiz.course.title}</td>
                                <td>${quiz.title}</td>
                                <td>${quiz.endDate}</td>
                                <td>${quiz.submissionCount}/${quiz.enrollmentCount}</td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty upcomingQuizzes}">
                            <tr>
                                <td colspan="4">No upcoming quiz deadlines</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>