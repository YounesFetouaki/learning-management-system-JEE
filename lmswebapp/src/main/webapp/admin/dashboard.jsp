<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        
        <div class="content">
            <div class="page-header">
                <h1>Admin Dashboard</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-primary">
                        <i class="icon-plus"></i> Add New User
                    </a>
                </div>
            </div>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <div class="stat-title">Total Users</div>
                    <div class="stat-value">${stats.totalUsers}</div>
                    <div class="stat-change ${stats.newUsers > 0 ? 'positive' : 'negative'}">
                        ${stats.newUsers > 0 ? '+' : ''}${stats.newUsers} new this week
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">Total Courses</div>
                    <div class="stat-value">${stats.totalCourses}</div>
                    <div class="stat-change ${stats.newCourses > 0 ? 'positive' : 'negative'}">
                        ${stats.newCourses > 0 ? '+' : ''}${stats.newCourses} new this week
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">Active Students</div>
                    <div class="stat-value">${stats.activeStudents}</div>
                    <div class="stat-change">
                        ${stats.studentActivityPercentage}% active this week
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">System Status</div>
                    <div class="stat-value">
                        <span class="status-indicator active">Operational</span>
                    </div>
                    <div class="stat-change">
                        Last checked: <fmt:formatDate value="${stats.lastChecked}" pattern="HH:mm:ss" />
                    </div>
                </div>
            </div>
            
            <div class="dashboard-charts">
                <div class="chart-container">
                    <h3>User Registration</h3>
                    <!-- Chart would go here in a real implementation -->
                    <div style="height: 200px; background-color: #f8f9fa; display: flex; align-items: center; justify-content: center;">
                        User registration chart placeholder
                    </div>
                </div>
                
                <div class="chart-container">
                    <h3>System Activity</h3>
                    <!-- Chart would go here in a real implementation -->
                    <div style="height: 200px; background-color: #f8f9fa; display: flex; align-items: center; justify-content: center;">
                        System activity chart placeholder
                    </div>
                </div>
            </div>
            
            <div class="dashboard-recent">
                <div class="card">
                    <div class="card-header">
                        <h2>Recent Users</h2>
                        <div class="card-tools">
                            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-sm btn-secondary">View All</a>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Created</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentUsers}" var="user">
                                        <tr>
                                            <td>${user.firstName} ${user.lastName}</td>
                                            <td>${user.email}</td>
                                            <td><span class="badge badge-${user.role == 'ADMIN' ? 'primary' : user.role == 'TEACHER' ? 'success' : 'info'}">${user.role}</span></td>
                                            <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
                                            <td><span class="status-indicator ${user.active ? 'active' : 'inactive'}">${user.active ? 'Active' : 'Inactive'}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h2>System Logs</h2>
                        <div class="card-tools">
                            <a href="${pageContext.request.contextPath}/admin/system/logs" class="btn btn-sm btn-secondary">View All</a>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Time</th>
                                        <th>Level</th>
                                        <th>Category</th>
                                        <th>Message</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentLogs}" var="log">
                                        <tr>
                                            <td><fmt:formatDate value="${log.timestamp}" pattern="HH:mm:ss" /></td>
                                            <td class="log-level-${log.level.toLowerCase()}">${log.level}</td>
                                            <td>${log.category}</td>
                                            <td>${log.message}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h2>Admin Quick Links</h2>
                </div>
                <div class="card-body">
                    <div class="admin-quick-links">
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">
                            User Management
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-primary">
                            Course Management
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/system/logs" class="btn btn-primary">
                            System Logs
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/system/settings" class="btn btn-primary">
                            System Settings
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/system/monitor" class="btn btn-primary">
                            System Monitor
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/backup" class="btn btn-primary">
                            Backup & Restore
                        </a>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
</body>
</html>