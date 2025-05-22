<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Learning Management System - My Courses</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>

    <div class="container">
        <jsp:include page="/includes/header.jsp" />

        <main>
            <h2>My Courses</h2>

            <c:if test="${empty courses}">
                <p>You are not enrolled in any courses.</p>
            </c:if>

            <c:if test="${not empty courses}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Course Title</th>
                            <th>Description</th>
                            <th>Teacher</th>
                            <th>Active</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td><c:out value="${course.title}" /></td>
                                <td><c:out value="${course.description}" /></td>
                                <td><c:out value="${course.teacher.fullName}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${course.active}">Yes</c:when>
                                        <c:otherwise>No</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </main>

        <jsp:include page="/includes/footer.jsp" />
    </div>

    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
