<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Learning Management System - Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>

    <div class="container">
        <jsp:include page="/includes/header.jsp" />

        <main>
            <h2>My Profile</h2>

            <c:choose>
                <c:when test="${not empty student}">
                    <table class="profile-table">
                        <tr>
                            <th>Full Name:</th>
                            <td><c:out value="${student.fullName}" /></td>
                        </tr>
                        <tr>
                            <th>Email:</th>
                            <td><c:out value="${student.email}" /></td>
                        </tr>
                        <tr>
                            <th>Role:</th>
                            <td><c:out value="${student.role}" /></td>
                        </tr>
                        <!-- Add more fields if needed -->
                    </table>
                </c:when>
                <c:otherwise>
                    <p>No profile information available.</p>
                </c:otherwise>
            </c:choose>
        </main>

        <jsp:include page="/includes/footer.jsp" />
    </div>

    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
