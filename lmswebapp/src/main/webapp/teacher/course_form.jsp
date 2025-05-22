<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>${course != null ? 'Edit Course' : 'Add New Course'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<div class="container">
    <jsp:include page="/includes/header.jsp" />

    <main>
        <h2>${course != null ? 'Edit Course' : 'Add New Course'}</h2>

        <form action="${pageContext.request.contextPath}/teacher/courses?action=${course != null ? 'update' : 'create'}" method="post">
            <c:if test="${course != null}">
                <input type="hidden" name="id" value="${course.id}" />
            </c:if>

            <label for="title">Title:</label><br/>
            <input type="text" id="title" name="title" value="${course != null ? course.title : ''}" required/><br/><br/>

            <label for="description">Description:</label><br/>
            <textarea id="description" name="description" required>${course != null ? course.description : ''}</textarea><br/><br/>

            <label for="active">Active:</label>
            <input type="checkbox" id="active" name="active" ${course != null && course.active ? 'checked' : ''}/><br/><br/>

            <button type="submit">Save</button>
            &nbsp;
            <a href="${pageContext.request.contextPath}/teacher/courses">Cancel</a>
        </form>

        <c:if test="${not empty error}">
            <div style="color:red; margin-top: 1em;">${error}</div>
        </c:if>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</div>

<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
