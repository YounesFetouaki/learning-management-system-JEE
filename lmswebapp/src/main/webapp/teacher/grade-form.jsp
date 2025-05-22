<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>
        <c:choose>
            <c:when test="${not empty grade}">Edit Grade</c:when>
            <c:otherwise>Add Grade</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>

<div class="container">
    <jsp:include page="/includes/header.jsp" />

    <main>
        <h1>
            <c:choose>
                <c:when test="${not empty grade}">Edit Grade</c:when>
                <c:otherwise>Add Grade</c:otherwise>
            </c:choose>
        </h1>

        <c:if test="${not empty error}">
            <p style="color:red;">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/teacher/grades" method="post">
            <input type="hidden" name="id" value="${grade.id}" />

            <label for="studentId">Student:</label>
            <select name="studentId" id="studentId" required>
                <option value="">-- Select Student --</option>
                <c:forEach var="student" items="${students}">
                    <option value="${student.id}" 
                        <c:if test="${grade != null && grade.student.id == student.id}">selected</c:if>>
                        ${student.fullName}
                    </option>
                </c:forEach>
            </select>
            <br/><br/>

            <label for="courseId">Course:</label>
            <select name="courseId" id="courseId" required>
                <option value="">-- Select Course --</option>
                <c:forEach var="course" items="${courses}">
                    <option value="${course.id}" 
                        <c:if test="${grade != null && grade.course.id == course.id}">selected</c:if>>
                        ${course.title}
                    </option>
                </c:forEach>
            </select>
            <br/><br/>

            <label for="quizId">Quiz:</label>
            <select name="quizId" id="quizId" required>
                <option value="">-- Select Quiz --</option>
                <c:forEach var="quiz" items="${quizzes}">
                    <option value="${quiz.id}" 
                        <c:if test="${grade != null && grade.quiz.id == quiz.id}">selected</c:if>>
                        ${quiz.title}
                    </option>
                </c:forEach>
            </select>
            <br/><br/>

            <label for="score">Score:</label>
            <input type="number" step="0.01" min="0" max="100" name="score" id="score" 
                   value="${grade != null ? grade.score : ''}" required/>
            <br/><br/>

            <button type="submit" name="action" value="${grade != null ? 'update' : 'create'}">
                ${grade != null ? 'Update' : 'Create'}
            </button>
            <a href="${pageContext.request.contextPath}/teacher/grades">Cancel</a>
        </form>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</div>

<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
