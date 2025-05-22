<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .error-container {
            text-align: center;
            padding: 3rem 1rem;
            max-width: 600px;
            margin: 0 auto;
        }
        
        .error-code {
            font-size: 6rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }
        
        .error-message {
            font-size: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .error-description {
            margin-bottom: 2rem;
            color: var(--gray-color);
        }
        
        .error-actions {
            display: flex;
            justify-content: center;
            gap: 1rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="error-container">
            <div class="error-code">404</div>
            <div class="error-message">Page Not Found</div>
            <div class="error-description">
                <p>The page you are looking for does not exist or has been moved.</p>
                <p>Please check the URL or navigate to another page.</p>
            </div>
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home</a>
                <c:if test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.userRole == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">Go to Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.userRole == 'TEACHER'}">
                            <a href="${pageContext.request.contextPath}/teacher/dashboard" class="btn btn-secondary">Go to Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.userRole == 'STUDENT'}">
                            <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-secondary">Go to Dashboard</a>
                        </c:when>
                    </c:choose>
                </c:if>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
</body>
</html>