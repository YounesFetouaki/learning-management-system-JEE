<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="dashboard">
            <h1>Admin Dashboard</h1>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <h3>Total Users</h3>
                    <p class="stat-number">${userCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Total Courses</h3>
                    <p class="stat-number">${courseCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Active Students</h3>
                    <p class="stat-number">${activeStudentCount}</p>
                </div>
                
                <div class="stat-card">
                    <h3>Active Teachers</h3>
                    <p class="stat-number">${activeTeacherCount}</p>
                </div>
            </div>
            
            <div class="dashboard-actions">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">Manage Users</a>
                    <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-primary">Manage Courses</a>
                    <a href="${pageContext.request.contextPath}/admin/system" class="btn btn-secondary">System Settings</a>
                </div>
            </div>
            
            <div class="recent-activity">
                <h2>Recent Activity</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>User</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentActivity}" var="activity">
                            <tr>
                                <td>${activity.date}</td>
                                <td>${activity.user}</td>
                                <td>${activity.action}</td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty recentActivity}">
                            <tr>
                                <td colspan="3">No recent activity</td>
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