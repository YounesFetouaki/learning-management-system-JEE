<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    
    <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>User List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
</head>
<body>
<div class="container">
    <jsp:include page="/includes/header.jsp" />
<h2>User List</h2>

<a href="${pageContext.request.contextPath}/admin/users?action=create">Add New User</a>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Full Name</th>
        <th>Role</th>
        <th>Active</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td>${user.fullName}</td>
            <td>${user.role}</td>
            <td>
                <c:choose>
                    <c:when test="${user.active}">Yes</c:when>
                    <c:otherwise>No</c:otherwise>
                </c:choose>
            </td>
            <td class="actions">
                <a href="${pageContext.request.contextPath}/admin/users?action=edit&id=${user.id}">Edit</a>
                <a href="${pageContext.request.contextPath}/admin/users?action=delete&id=${user.id}" onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="/includes/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>
