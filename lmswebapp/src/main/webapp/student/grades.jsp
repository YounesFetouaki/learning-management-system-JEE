<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Learning Management System - Courses</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>

    <div class="container">
        <jsp:include page="/includes/header.jsp" />

<h1>My Grades</h1>

<c:choose>
    <c:when test="${not empty grades}">
        <table border="1" cellpadding="10" cellspacing="0">
            <thead>
                <tr>
                    <th>Course</th>
                    <th>Quiz</th>
                    <th>Score</th>
                    <th>Feedback</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="grade" items="${grades}">
                    <tr>
                        <td><c:out value="${grade.course.title}" /></td>
                        <td><c:out value="${grade.quiz != null ? grade.quiz.title : 'N/A'}" /></td>
                        <td><fmt:formatNumber value="${grade.score}" maxFractionDigits="2" /></td>
                        <td><c:out value="${grade.feedback != null ? grade.feedback : '-'}" /></td>
                        <td><fmt:formatDate value="${grade.createdAt}" pattern="yyyy-MM-dd" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p>
            Average Grade: 
            <strong>
                <fmt:formatNumber value="${averageGrade}" type="number" maxFractionDigits="2" />
            </strong>
        </p>

    </c:when>
    <c:otherwise>
        <p>No grades available.</p>
    </c:otherwise>
</c:choose>

<jsp:include page="/includes/footer.jsp" />
</body>
</html>
