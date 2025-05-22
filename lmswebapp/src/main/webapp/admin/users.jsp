<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>User Management</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-primary">
                        <i class="icon-plus"></i> Add New User
                    </a>
                </div>
            </div>
            
            <c:if test="${not empty success}">
                <div class="success-message">
                    <p>${success}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-header">
                    <h2>All Users</h2>
                    <div class="card-tools">
                        <input type="text" id="userSearch" placeholder="Search users..." class="search-input">
                    </div>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="data-table" id="usersTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Created</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td>${user.id}</td>
                                        <td>${user.firstName} ${user.lastName}</td>
                                        <td>${user.email}</td>
                                        <td><span class="badge badge-${user.role == 'ADMIN' ? 'primary' : user.role == 'TEACHER' ? 'success' : 'info'}">${user.role}</span></td>
                                        <td><span class="status-indicator ${user.active ? 'active' : 'inactive'}">${user.active ? 'Active' : 'Inactive'}</span></td>
                                        <td>${user.createdAt}</td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/admin/users/edit/${user.id}" class="btn btn-sm btn-secondary">Edit</a>
                                            <button class="btn btn-sm btn-danger" onclick="confirmDelete(${user.id}, '${user.firstName} ${user.lastName}')">Delete</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Deletion</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete the user <span id="userName"></span>?</p>
                <p>This action cannot be undone.</p>
            </div>
            <div class="modal-footer">
                <button id="cancelDelete" class="btn btn-secondary">Cancel</button>
                <form id="deleteForm" method="post">
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        // Search functionality
        document.getElementById('userSearch').addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const table = document.getElementById('usersTable');
            const rows = table.getElementsByTagName('tr');
            
            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const cells = row.getElementsByTagName('td');
                let found = false;
                
                for (let j = 0; j < cells.length - 1; j++) {
                    const cellText = cells[j].textContent.toLowerCase();
                    if (cellText.indexOf(searchValue) > -1) {
                        found = true;
                        break;
                    }
                }
                
                row.style.display = found ? '' : 'none';
            }
        });
        
        // Delete confirmation modal
        const modal = document.getElementById('deleteModal');
        const closeBtn = document.getElementsByClassName('close')[0];
        const cancelBtn = document.getElementById('cancelDelete');
        
        function confirmDelete(userId, name) {
            document.getElementById('userName').textContent = name;
            document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/admin/users/delete/' + userId;
            modal.style.display = 'block';
        }
        
        closeBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        cancelBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    </script>
</body>
</html>