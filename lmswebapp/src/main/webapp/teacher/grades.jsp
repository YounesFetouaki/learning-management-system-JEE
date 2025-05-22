<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Teacher - Grades Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>

<div class="container">
    <jsp:include page="/includes/header.jsp" />

    <main>
        <h2>Grades Management</h2>

        <p>
            <a href="${pageContext.request.contextPath}/teacher/grades?action=new" class="btn btn-primary">
                Add New Grade
            </a>
        </p>

        <c:if test="${empty grades}">
            <p>No grades found.</p>
        </c:if>

        <c:if test="${not empty grades}">
            <table class="table">
                <thead>
                    <tr>
                        <th>Student</th>
                        <th>Quiz</th>
                        <th>Course</th>
                        <th>Score</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="grade" items="${grades}">
                        <tr>
                            <td><c:out value="${grade.student.fullName}" /></td>
                            <td><c:out value="${grade.quiz.title}" /></td>
                            <td><c:out value="${grade.course.title}" /></td>
                            <td><c:out value="${grade.score}" /></td>
                            <td><fmt:formatDate value="${grade.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/teacher/grades?action=edit&id=${grade.id}">Edit</a>
                                |
                                <a href="${pageContext.request.contextPath}/teacher/grades?action=delete&id=${grade.id}"
                                   onclick="return confirm('Are you sure you want to delete this grade?');">
                                   Delete
                                </a>
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
