<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>My Courses</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<div class="container">
    <jsp:include page="/includes/header.jsp" />

    <main>
        <h2>My Courses</h2>

        <c:choose>
            <c:when test="${empty courses}">
                <p>You have no courses yet.</p>
            </c:when>
            <c:otherwise>
                <table class="profile-table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Description</th>
                            <th>Active</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td><c:out value="${course.title}" /></td>
                                <td><c:out value="${course.description}" /></td>
                                <td><c:out value="${course.active}" /></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher/courses?action=edit&id=${course.id}">Edit</a> |
                                    <a href="${pageContext.request.contextPath}/teacher/courses?action=delete&id=${course.id}" onclick="return confirm('Are you sure?');">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <p>
            <a href="${pageContext.request.contextPath}/teacher/courses?action=new">Add New Course</a>
        </p>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</div>

<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
