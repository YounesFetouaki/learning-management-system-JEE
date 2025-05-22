<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Learning Management System - Quizzes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<div class="container">

    <jsp:include page="/includes/header.jsp" />

    <main>
        <h2>Quizzes</h2>

        <c:if test="${empty quizzes}">
            <p>No quizzes found.</p>
        </c:if>

        <c:if test="${not empty quizzes}">
            <table class="table">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Course</th>
                    <th>Active</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="quiz" items="${quizzes}">
                    <tr>
                        <td><c:out value="${quiz.title}" /></td>
                        <td><c:out value="${quiz.description}" /></td>
                        <td><c:out value="${quiz.course.title}" /></td>
                        <td><c:out value="${quiz.active ? 'Yes' : 'No'}" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/teacher/quizzes?action=edit&id=${quiz.id}">Edit</a> |
                            <a href="${pageContext.request.contextPath}/teacher/quizzes?action=delete&id=${quiz.id}" 
                               onclick="return confirm('Are you sure you want to delete this quiz?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <p><a class="btn" href="${pageContext.request.contextPath}/teacher/quizzes?action=new">Add New Quiz</a></p>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</div>

<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
