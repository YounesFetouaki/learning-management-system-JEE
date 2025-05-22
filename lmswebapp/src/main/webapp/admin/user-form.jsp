<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty user ? 'Create' : 'Edit'} User - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>${empty user ? 'Create New' : 'Edit'} User</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                        <i class="icon-arrow-left"></i> Back to Users
                    </a>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="error-message">
                    <p>${error}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/users${empty user ? '' : '/edit/'.concat(user.id)}" method="post" class="form">
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="firstName">First Name</label>
                                <input type="text" id="firstName" name="firstName" value="${user.firstName}" required>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="lastName">Last Name</label>
                                <input type="text" id="lastName" name="lastName" value="${user.lastName}" required>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="${user.email}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="password">Password ${empty user ? '' : '(Leave blank to keep current password)'}</label>
                            <input type="password" id="password" name="password" ${empty user ? 'required' : ''}>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="role">Role</label>
                                <select id="role" name="role" required>
                                    <option value="">Select Role</option>
                                    <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Administrator</option>
                                    <option value="TEACHER" ${user.role == 'TEACHER' ? 'selected' : ''}>Teacher</option>
                                    <option value="STUDENT" ${user.role == 'STUDENT' ? 'selected' : ''}>Student</option>
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label class="checkbox-container">
                                    <input type="checkbox" id="active" name="active" ${empty user || user.active ? 'checked' : ''}>
                                    <span class="checkmark"></span>
                                    Active
                                </label>
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">${empty user ? 'Create' : 'Update'} User</button>
                            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
</body>
</html>