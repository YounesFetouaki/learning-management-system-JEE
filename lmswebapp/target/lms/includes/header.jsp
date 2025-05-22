<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header>
    <div class="header-container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/">Learning Management System</a>
        </div>
        
        <nav>
            <ul class="nav-menu">
                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/courses">Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/system">System</a></li>
                </c:if>
                
                <c:if test="${sessionScope.userRole == 'TEACHER'}">
                    <li><a href="${pageContext.request.contextPath}/teacher/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/teacher/courses">Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/teacher/quizzes">Quizzes</a></li>
                    <li><a href="${pageContext.request.contextPath}/teacher/grades">Grades</a></li>
                </c:if>
                
                <c:if test="${sessionScope.userRole == 'STUDENT'}">
                    <li><a href="${pageContext.request.contextPath}/student/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/student/courses">Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/student/grades">Grades</a></li>
                </c:if>
                
                <!-- Calendar link for all roles -->
                <li><a href="${pageContext.request.contextPath}/calendar">Calendar</a></li>
            </ul>
        </nav>
        
        <div class="user-menu">
            <c:if test="${not empty sessionScope.user}">
                <div class="user-info">
                    <span>${sessionScope.user.fullName}</span>
                    <span class="user-role">(${sessionScope.userRole})</span>
                </div>
                <div class="user-actions">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Profile</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
                </div>
            </c:if>
        </div>
    </div>
</header>