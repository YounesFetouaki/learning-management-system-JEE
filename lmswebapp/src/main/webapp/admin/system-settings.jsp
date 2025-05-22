<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Settings - LMS Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .settings-tabs {
            display: flex;
            border-bottom: 1px solid #ddd;
            margin-bottom: 1.5rem;
        }
        
        .settings-tab {
            padding: 0.75rem 1rem;
            cursor: pointer;
            border-bottom: 2px solid transparent;
            margin-right: 1rem;
        }
        
        .settings-tab.active {
            border-bottom-color: #3498db;
            font-weight: 600;
        }
        
        .settings-section {
            display: none;
        }
        
        .settings-section.active {
            display: block;
        }
        
        .system-info-item {
            display: flex;
            margin-bottom: 0.5rem;
        }
        
        .system-info-label {
            font-weight: 600;
            width: 200px;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>System Settings</h1>
            </div>
            
            <c:if test="${not empty sessionScope.success}">
                <div class="success-message">
                    <p>${sessionScope.success}</p>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>
            
            <div class="card">
                <div class="card-body">
                    <div class="settings-tabs">
                        <div class="settings-tab active" onclick="showSettingsTab('general')">General</div>
                        <div class="settings-tab" onclick="showSettingsTab('email')">Email</div>
                        <div class="settings-tab" onclick="showSettingsTab('security')">Security</div>
                        <div class="settings-tab" onclick="showSettingsTab('uploads')">File Uploads</div>
                        <div class="settings-tab" onclick="showSettingsTab('system')">System Information</div>
                    </div>
                    
                    <form action="${pageContext.request.contextPath}/admin/system/settings" method="post">
                        <!-- General Settings -->
                        <div id="general-settings" class="settings-section active">
                            <h3>General Settings</h3>
                            
                            <div class="form-group">
                                <label for="app.name">Application Name</label>
                                <input type="text" id="app.name" name="app.name" value="${settings['app.name']}" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="app.version">Application Version</label>
                                <input type="text" id="app.version" name="app.version" value="${settings['app.version']}" readonly>
                                <small class="form-text">Version cannot be changed through the UI</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="app.url">Application URL</label>
                                <input type="url" id="app.url" name="app.url" value="${settings['app.url']}">
                            </div>
                        </div>
                        
                        <!-- Email Settings -->
                        <div id="email-settings" class="settings-section">
                            <h3>Email Settings</h3>
                            
                            <div class="form-group">
                                <label class="checkbox-container">
                                    <input type="checkbox" id="mail.enabled" name="mail.enabled" ${settings['mail.enabled'] == 'true' ? 'checked' : ''}>
                                    <span class="checkmark"></span>
                                    Enable Email Notifications
                                </label>
                            </div>
                            
                            <div class="form-group">
                                <label for="mail.smtp.host">SMTP Host</label>
                                <input type="text" id="mail.smtp.host" name="mail.smtp.host" value="${settings['mail.smtp.host']}">
                            </div>
                            
                            <div class="form-group">
                                <label for="mail.smtp.port">SMTP Port</label>
                                <input type="number" id="mail.smtp.port" name="mail.smtp.port" value="${settings['mail.smtp.port']}">
                            </div>
                            
                            <div class="form-group">
                                <label for="mail.from">From Email Address</label>
                                <input type="email" id="mail.from" name="mail.from" value="${settings['mail.from']}">
                            </div>
                        </div>
                        
                        <!-- Security Settings -->
                        <div id="security-settings" class="settings-section">
                            <h3>Security Settings</h3>
                            
                            <div class="form-group">
                                <label for="security.password.min_length">Minimum Password Length</label>
                                <input type="number" id="security.password.min_length" name="security.password.min_length" value="${settings['security.password.min_length']}" min="6" max="20">
                            </div>
                            
                            <div class="form-group">
                                <label for="security.session.timeout">Session Timeout (minutes)</label>
                                <input type="number" id="security.session.timeout" name="security.session.timeout" value="${settings['security.session.timeout']}" min="5" max="240">
                            </div>
                            
                            <div class="form-group">
                                <label for="security.login.max_attempts">Maximum Login Attempts</label>
                                <input type="number" id="security.login.max_attempts" name="security.login.max_attempts" value="${settings['security.login.max_attempts']}" min="3" max="10">
                            </div>
                        </div>
                        
                        <!-- File Upload Settings -->
                        <div id="uploads-settings" class="settings-section">
                            <h3>File Upload Settings</h3>
                            
                            <div class="form-group">
                                <label for="upload.max_size">Maximum File Size (bytes)</label>
                                <input type="number" id="upload.max_size" name="upload.max_size" value="${settings['upload.max_size']}" min="1048576">
                                <small class="form-text">Current value: ${Integer.parseInt(settings['upload.max_size']) / 1048576} MB</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="upload.allowed_types">Allowed File Types</label>
                                <input type="text" id="upload.allowed_types" name="upload.allowed_types" value="${settings['upload.allowed_types']}">
                                <small class="form-text">Comma-separated list of file extensions (e.g., pdf,doc,jpg)</small>
                            </div>
                        </div>
                        
                        <!-- System Information -->
                        <div id="system-settings" class="settings-section">
                            <h3>System Information</h3>
                            
                            <div class="system-info">
                                <div class="system-info-item">
                                    <div class="system-info-label">Java Version:</div>
                                    <div>${systemInfo['java.version']}</div>
                                </div>
                                
                                <div class="system-info-item">
                                    <div class="system-info-label">Java Vendor:</div>
                                    <div>${systemInfo['java.vendor']}</div>
                                </div>
                                
                                <div class="system-info-item">
                                    <div class="system-info-label">Operating System:</div>
                                    <div>${systemInfo['os.name']} ${systemInfo['os.version']}</div>
                                </div>
                                
                                <div class="system-info-item">
                                    <div class="system-info-label">Timezone:</div>
                                    <div>${systemInfo['user.timezone']}</div>
                                </div>
                                
                                <div class="system-info-item">
                                    <div class="system-info-label">File Encoding:</div>
                                    <div>${systemInfo['file.encoding']}</div>
                                </div>
                                
                                <div class="system-info-item">
                                    <div class="system-info-label">Tomcat Home:</div>
                                    <div>${systemInfo['catalina.home']}</div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save Settings</button>
                            <button type="reset" class="btn btn-secondary">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script>
        function showSettingsTab(tabId) {
            // Hide all sections
            const sections = document.querySelectorAll('.settings-section');
            sections.forEach(section => {
                section.classList.remove('active');
            });
            
            // Deactivate all tabs
            const tabs = document.querySelectorAll('.settings-tab');
            tabs.forEach(tab => {
                tab.classList.remove('active');
            });
            
            // Show selected section
            document.getElementById(tabId + '-settings').classList.add('active');
            
            // Activate selected tab
            event.currentTarget.classList.add('active');
        }
    </script>
</body>
</html>