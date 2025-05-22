<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
&lt;!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Files - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>Course Files</h1>
                <div class="page-actions">
                    <c:if test="${not empty selectedCourse}">
                        <button class="btn btn-primary" onclick="showUploadModal()">
                            <i class="icon-upload"></i> Upload File
                        </button>
                    </c:if>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="error-message">
                    <p>${error}</p>
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="success-message">
                    <p>${success}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-header">
                    <h2>Select Course</h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/teacher/files" method="get" class="form-inline">
                        <div class="form-group">
                            <label for="courseId">Course:</label>
                            <select id="courseId" name="courseId" class="form-control" onchange="this.form.submit()">
                                <option value="">Select a course</option>
                                <c:forEach items="${courses}" var="course">
                                    <option value="${course.id}" ${selectedCourse.id == course.id ? 'selected' : ''}>${course.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
            
            <c:if test="${not empty selectedCourse}">
                <div class="card mt-4">
                    <div class="card-header">
                        <h2>Files for ${selectedCourse.title}</h2>
                        <div class="card-tools">
                            <input type="text" id="fileSearch" placeholder="Search files..." class="search-input">
                        </div>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty files}">
                                <div class="table-responsive">
                                    <table class="data-table" id="filesTable">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>File Name</th>
                                                <th>Type</th>
                                                <th>Size</th>
                                                <th>Uploaded</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${files}" var="file">
                                                <tr>
                                                    <td>
                                                        <div class="file-title">
                                                            <div class="file-icon file-icon-${file.fileExtension}"></div>
                                                            <span>${file.title}</span>
                                                        </div>
                                                        <c:if test="${not empty file.description}">
                                                            <div class="file-description">${file.description}</div>
                                                        </c:if>
                                                    </td>
                                                    <td>${file.fileName}</td>
                                                    <td>${file.fileExtension.toUpperCase()}</td>
                                                    <td>${file.formattedFileSize}</td>
                                                    <td><fmt:formatDate value="${file.uploadDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                    <td class="actions">
                                                        <a href="${pageContext.request.contextPath}/teacher/files/download/${file.id}" class="btn btn-sm btn-primary">Download</a>
                                                        <button class="btn btn-sm btn-danger" onclick="confirmDelete(${file.id}, '${file.title}')">Delete</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <p>No files have been uploaded to this course yet.</p>
                                    <button class="btn btn-primary" onclick="showUploadModal()">Upload a File</button>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    &lt;!-- File Upload Modal -->
    <div id="uploadModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Upload File</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/teacher/files" method="post" enctype="multipart/form-data" class="form">
                    <input type="hidden" name="courseId" value="${selectedCourse.id}">
                    
                    <div class="form-group">
                        <label for="title">Title</label>
                        <input type="text" id="title" name="title" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description (optional)</label>
                        <textarea id="description" name="description" rows="3"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="file">File</label>
                        <input type="file" id="file" name="file" required>
                        <div class="file-info">
                            <p>Maximum file size: 10 MB</p>
                            <p>Supported file types: PDF, DOC, DOCX, PPT, PPTX, XLS, XLSX, TXT, ZIP, RAR, JPG, PNG, GIF</p>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Upload</button>
                        <button type="button" class="btn btn-secondary" onclick="hideUploadModal()">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    &lt;!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Deletion</h3>
                <span class="close delete-close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete the file <span id="fileName"></span>?</p>
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
        document.getElementById('fileSearch').addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const table = document.getElementById('filesTable');
            const rows = table.getElementsByTagName('tr');
            
            for (let i = 1; i &lt; rows.length; i++) {
                const row = rows[i];
                const cells = row.getElementsByTagName('td');
                let found = false;
                
                for (let j = 0; j &lt; cells.length - 1; j++) {
                    const cellText = cells[j].textContent.toLowerCase();
                    if (cellText.indexOf(searchValue) > -1) {
                        found = true;
                        break;
                    }
                }
                
                row.style.display = found ? '' : 'none';
            }
        });
        
        // Upload modal
        const uploadModal = document.getElementById('uploadModal');
        const uploadCloseBtn = uploadModal.getElementsByClassName('close')[0];
        
        function showUploadModal() {
            uploadModal.style.display = 'block';
        }
        
        function hideUploadModal() {
            uploadModal.style.display = 'none';
        }
        
        uploadCloseBtn.onclick = hideUploadModal;
        
        // Delete confirmation modal
        const deleteModal = document.getElementById('deleteModal');
        const deleteCloseBtn = document.getElementsByClassName('delete-close')[0];
        const cancelDeleteBtn = document.getElementById('cancelDelete');
        
        function confirmDelete(fileId, title) {
            document.getElementById('fileName').textContent = title;
            document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/teacher/files/delete/' + fileId;
            deleteModal.style.display = 'block';
        }
        
        deleteCloseBtn.onclick = function() {
            deleteModal.style.display = 'none';
        }
        
        cancelDeleteBtn.onclick = function() {
            deleteModal.style.display = 'none';
        }
        
        // Close modals when clicking outside
        window.onclick = function(event) {
            if (event.target == uploadModal) {
                uploadModal.style.display = 'none';
            }
            if (event.target == deleteModal) {
                deleteModal.style.display = 'none';
            }
        }
        
        // File input validation
        document.getElementById('file').addEventListener('change', function() {
            const fileInput = this;
            const filePath = fileInput.value;
            const allowedExtensions = /(\.pdf|\.doc|\.docx|\.ppt|\.pptx|\.xls|\.xlsx|\.txt|\.zip|\.rar|\.jpg|\.jpeg|\.png|\.gif)$/i;
            
            if (!allowedExtensions.exec(filePath)) {
                alert('Please upload a file with a supported file type.');
                fileInput.value = '';
                return false;
            }
            
            if (fileInput.files[0].size > 10 * 1024 * 1024) {
                alert('File size exceeds 10 MB. Please choose a smaller file.');
                fileInput.value = '';
                return false;
            }
        });
    </script>
</body>
</html>