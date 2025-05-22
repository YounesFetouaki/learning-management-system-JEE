<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Logs - LMS Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .log-filters {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        
        .log-level-info {
            color: #3498db;
        }
        
        .log-level-warn {
            color: #f39c12;
        }
        
        .log-level-error {
            color: #e74c3c;
        }
        
        .log-level-debug {
            color: #95a5a6;
        }
        
        .log-details-toggle {
            cursor: pointer;
            color: #3498db;
            text-decoration: underline;
        }
        
        .log-details {
            display: none;
            background-color: #f8f9fa;
            padding: 0.75rem;
            margin-top: 0.5rem;
            border-radius: 4px;
            font-family: monospace;
            white-space: pre-wrap;
        }
        
        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 1.5rem;
        }
        
        .pagination a, .pagination span {
            padding: 0.5rem 0.75rem;
            margin: 0 0.25rem;
            border: 1px solid #ddd;
            text-decoration: none;
            border-radius: 4px;
        }
        
        .pagination a:hover {
            background-color: #f5f5f5;
        }
        
        .pagination .active {
            background-color: #3498db;
            color: white;
            border-color: #3498db;
        }
        
        .pagination .disabled {
            color: #aaa;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>System Logs</h1>
                <div class="page-actions">
                    <form action="${pageContext.request.contextPath}/admin/system/logs" method="post">
                        <input type="hidden" name="action" value="export">
                        <button type="submit" class="btn btn-secondary">
                            <i class="icon-download"></i> Export Logs
                        </button>
                    </form>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h2>Log Filters</h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/system/logs" method="get" id="logFilterForm">
                        <div class="log-filters">
                            <div class="form-group">
                                <label for="category">Category</label>
                                <select id="category" name="category" class="form-control">
                                    <option value="">All Categories</option>
                                    <option value="SECURITY" ${category == 'SECURITY' ? 'selected' : ''}>Security</option>
                                    <option value="USER_ACTIVITY" ${category == 'USER_ACTIVITY' ? 'selected' : ''}>User Activity</option>
                                    <option value="SYSTEM" ${category == 'SYSTEM' ? 'selected' : ''}>System</option>
                                    <option value="DATABASE" ${category == 'DATABASE' ? 'selected' : ''}>Database</option>
                                    <option value="PERFORMANCE" ${category == 'PERFORMANCE' ? 'selected' : ''}>Performance</option>
                                    <option value="AUDIT" ${category == 'AUDIT' ? 'selected' : ''}>Audit</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="level">Level</label>
                                <select id="level" name="level" class="form-control">
                                    <option value="">All Levels</option>
                                    <option value="INFO" ${level == 'INFO' ? 'selected' : ''}>Info</option>
                                    <option value="WARN" ${level == 'WARN' ? 'selected' : ''}>Warning</option>
                                    <option value="ERROR" ${level == 'ERROR' ? 'selected' : ''}>Error</option>
                                    <option value="DEBUG" ${level == 'DEBUG' ? 'selected' : ''}>Debug</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="userId">User</label>
                                <select id="userId" name="userId" class="form-control">
                                    <option value="">All Users</option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.id}" ${userId == user.id.toString() ? 'selected' : ''}>${user.firstName} ${user.lastName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="startDate">Start Date</label>
                                <input type="date" id="startDate" name="startDate" value="${startDate}" class="form-control">
                            </div>
                            
                            <div class="form-group">
                                <label for="endDate">End Date</label>
                                <input type="date" id="endDate" name="endDate" value="${endDate}" class="form-control">
                            </div>
                            
                            <div class="form-group">
                                <label for="keyword">Keyword</label>
                                <input type="text" id="keyword" name="keyword" value="${keyword}" placeholder="Search logs..." class="form-control">
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Apply Filters</button>
                            <button type="button" class="btn btn-secondary" onclick="clearFilters()">Clear Filters</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h2>Log Entries</h2>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty logs}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Timestamp</th>
                                            <th>Level</th>
                                            <th>Category</th>
                                            <th>User</th>
                                            <th>Action</th>
                                            <th>Message</th>
                                            <th>IP Address</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${logs}" var="log">
                                            <tr>
                                                <td><fmt:formatDate value="${log.timestamp}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td class="log-level-${log.level.toLowerCase()}">${log.level}</td>
                                                <td>${log.category}</td>
                                                <td>
                                                    <c:if test="${log.userId != null}">
                                                        <a href="${pageContext.request.contextPath}/admin/users/edit/${log.userId}">User #${log.userId}</a>
                                                    </c:if>
                                                </td>
                                                <td>${log.action}</td>
                                                <td>
                                                    ${log.message}
                                                    <c:if test="${not empty log.details}">
                                                        <div>
                                                            <span class="log-details-toggle" onclick="toggleDetails(${log.id})">Show Details</span>
                                                            <div id="details-${log.id}" class="log-details">${log.details}</div>
                                                        </div>
                                                    </c:if>
                                                </td>
                                                <td>${log.ipAddress}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <!-- Pagination -->
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="${pageContext.request.contextPath}/admin/system/logs?page=${currentPage - 1}${not empty category ? '&category='.concat(category) : ''}${not empty level ? '&level='.concat(level) : ''}${not empty userId ? '&userId='.concat(userId) : ''}${not empty startDate ? '&startDate='.concat(startDate) : ''}${not empty endDate ? '&endDate='.concat(endDate) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}">&laquo; Previous</a>
                                </c:if>
                                
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${currentPage == i}">
                                            <span class="active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/system/logs?page=${i}${not empty category ? '&category='.concat(category) : ''}${not empty level ? '&level='.concat(level) : ''}${not empty userId ? '&userId='.concat(userId) : ''}${not empty startDate ? '&startDate='.concat(startDate) : ''}${not empty endDate ? '&endDate='.concat(endDate) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                
                                <c:if test="${currentPage < totalPages}">
                                    <a href="${pageContext.request.contextPath}/admin/system/logs?page=${currentPage + 1}${not empty category ? '&category='.concat(category) : ''}${not empty level ? '&level='.concat(level) : ''}${not empty userId ? '&userId='.concat(userId) : ''}${not empty startDate ? '&startDate='.concat(startDate) : ''}${not empty endDate ? '&endDate='.concat(endDate) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}">Next &raquo;</a>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <p>No log entries found matching the current filters.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script>
        function toggleDetails(logId) {
            const detailsElement = document.getElementById('details-' + logId);
            if (detailsElement.style.display === 'block') {
                detailsElement.style.display = 'none';
            } else {
                detailsElement.style.display = 'block';
            }
        }
        
        function clearFilters() {
            document.getElementById('category').value = '';
            document.getElementById('level').value = '';
            document.getElementById('userId').value = '';
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            document.getElementById('keyword').value = '';
            document.getElementById('logFilterForm').submit();
        }
        
        // Validate date range
        document.getElementById('logFilterForm').addEventListener('submit', function(e) {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            
            if (startDate && endDate && startDate > endDate) {
                e.preventDefault();
                alert('End date must be after start date');
            }
        });
    </script>
</body>
</html>