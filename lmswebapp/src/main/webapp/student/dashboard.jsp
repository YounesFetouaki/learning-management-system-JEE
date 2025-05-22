<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="dashboard">
            <h1>Student Dashboard</h1>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <h3>My Courses</h3>
                    <p class="stat-number">${enrolledCoursesCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Completed Quizzes</h3>
                    <p class="stat-number">${completedQuizzesCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Pending Quizzes</h3>
                    <p class="stat-number">${pendingQuizzesCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Average Grade</h3>
                    <p class="stat-number">${averageGrade}%</p>
                </div>
            </div>
            
            <div class="dashboard-actions">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/student/courses" class="btn btn-primary">My Courses</a>
                    <a href="${pageContext.request.contextPath}/student/grades" class="btn btn-primary">My Grades</a>
                </div>
            </div>
            
            <div class="upcoming-quizzes">
                <h2>Upcoming Quizzes</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Quiz</th>
                            <th>Due Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${upcomingQuizzes}" var="quiz">
                            <tr>
                                <td>${quiz.course.title}</td>
                                <td>${quiz.title}</td>
                                <td>${quiz.endDate}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/student/quiz/take/${quiz.id}" class="btn btn-secondary">Take Quiz</a>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty upcomingQuizzes}">
                            <tr>
                                <td colspan="4">No upcoming quizzes</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="recent-grades">
                <h2>Recent Grades</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Course</th>
                            <th>Quiz</th>
                            <th>Score</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentGrades}" var="grade">
                            <tr>
                                <td>${grade.createdAt}</td>
                                <td>${grade.course.title}</td>
                                <td>${grade.quiz != null ? grade.quiz.title : 'Course Grade'}</td>
                                <td class="${grade.score >= 70 ? 'passing-grade' : 'failing-grade'}">${grade.score}%</td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty recentGrades}">
                            <tr>
                                <td colspan="4">No recent grades</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="course-announcements">
                <h2>Course Announcements</h2>
                <div class="announcements-list">
                    <c:forEach items="${courseAnnouncements}" var="announcement">
                        <div class="announcement-card">
                            <h3>${announcement.course.title}</h3>
                            <p class="announcement-date">${announcement.createdAt}</p>
                            <p class="announcement-content">${announcement.content}</p>
                        </div>
                    </c:forEach>
                    
                    <c:if test="${empty courseAnnouncements}">
                        <div class="empty-state">
                            <p>No announcements at this time.</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>