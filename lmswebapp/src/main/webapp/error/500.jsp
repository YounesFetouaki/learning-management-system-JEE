<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error - LMS</title>
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
            color: var(--danger-color);
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
        
        .error-details {
            margin: 2rem 0;
            text-align: left;
            background-color: #f8f9fa;
            padding: 1rem;
            border-radius: 0.25rem;
            overflow: auto;
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
            <div class="error-code">500</div>
            <div class="error-message">Internal Server Error</div>
            <div class="error-description">
                <p>Something went wrong on our end. We're working to fix the issue.</p>
                <p>Please try again later or contact support if the problem persists.</p>
            </div>
            
            <c:if test="${pageContext.errorData.throwable != null && sessionScope.userRole == 'ADMIN'}">
                <div class="error-details">
                    <h3>Error Details (Admin Only):</h3>
                    <p><strong>Error Message:</strong> ${pageContext.errorData.throwable.message}</p>
                    <p><strong>Servlet Name:</strong> ${pageContext.errorData.servletName}</p>
                    <p><strong>Request URI:</strong> ${pageContext.errorData.requestURI}</p>
                    <p><strong>Status Code:</strong> ${pageContext.errorData.statusCode}</p>
                    <p><strong>Stack Trace:</strong></p>
                    <pre>
                        <c:forEach var="stackTraceElement" items="${pageContext.errorData.throwable.stackTrace}">
                            ${stackTraceElement}
                        </c:forEach>
                    </pre>
                </div>
            </c:if>
            
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
                <button onclick="window.location.reload();" class="btn btn-secondary">Try Again</button>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
</body>
</html>