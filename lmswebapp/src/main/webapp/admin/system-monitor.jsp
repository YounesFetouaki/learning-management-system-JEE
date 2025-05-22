<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Monitor - LMS Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .monitor-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1.5rem;
            margin-bottom: 1.5rem;
        }
        
        .monitor-card {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
        }
        
        .monitor-card h3 {
            margin-top: 0;
            margin-bottom: 1rem;
            font-size: 1.25rem;
            color: #2c3e50;
            border-bottom: 1px solid #eee;
            padding-bottom: 0.5rem;
        }
        
        .monitor-stat {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.75rem;
        }
        
        .monitor-stat-label {
            font-weight: 500;
            color: #7f8c8d;
        }
        
        .monitor-stat-value {
            font-weight: 600;
        }
        
        .progress-bar {
            height: 8px;
            background-color: #ecf0f1;
            border-radius: 4px;
            margin-top: 0.5rem;
            overflow: hidden;
        }
        
        .progress-bar-fill {
            height: 100%;
            background-color: #3498db;
            border-radius: 4px;
        }
        
        .progress-bar-fill.warning {
            background-color: #f39c12;
        }
        
        .progress-bar-fill.danger {
            background-color: #e74c3c;
        }
        
        .refresh-button {
            margin-bottom: 1.5rem;
        }
        
        .auto-refresh {
            display: flex;
            align-items: center;
            margin-left: 1rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>System Monitor</h1>
                <div class="page-actions">
                    <button id="refreshButton" class="btn btn-secondary refresh-button" onclick="refreshPage()">
                        <i class="icon-refresh"></i> Refresh
                    </button>
                    <div class="auto-refresh">
                        <label class="checkbox-container">
                            <input type="checkbox" id="autoRefresh" onchange="toggleAutoRefresh()">
                            <span class="checkmark"></span>
                            Auto-refresh
                        </label>
                        <select id="refreshInterval" onchange="updateRefreshInterval()" disabled>
                            <option value="5000">5 seconds</option>
                            <option value="10000" selected>10 seconds</option>
                            <option value="30000">30 seconds</option>
                            <option value="60000">1 minute</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <div class="monitor-grid">
                <!-- Memory Usage -->
                <div class="monitor-card">
                    <h3>Memory Usage</h3>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Heap Used:</div>
                        <div class="monitor-stat-value">${systemMetrics.heapUsed}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Heap Max:</div>
                        <div class="monitor-stat-value">${systemMetrics.heapMax}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Non-Heap Used:</div>
                        <div class="monitor-stat-value">${systemMetrics.nonHeapUsed}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Heap Usage:</div>
                        <div class="monitor-stat-value">${systemMetrics.heapUsedPercentage}%</div>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill ${systemMetrics.heapUsedPercentage > 80 ? 'danger' : systemMetrics.heapUsedPercentage > 60 ? 'warning' : ''}" style="width: ${systemMetrics.heapUsedPercentage}%"></div>
                    </div>
                </div>
                
                <!-- CPU Information -->
                <div class="monitor-card">
                    <h3>CPU Information</h3>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Available Processors:</div>
                        <div class="monitor-stat-value">${systemMetrics.availableProcessors}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">System Load Average:</div>
                        <div class="monitor-stat-value">${systemMetrics.systemLoadAverage >= 0 ? systemMetrics.systemLoadAverage : 'N/A'}</div>
                    </div>
                </div>
                
                <!-- Runtime Information -->
                <div class="monitor-card">
                    <h3>Runtime Information</h3>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">JVM Name:</div>
                        <div class="monitor-stat-value">${systemMetrics.jvmName}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">JVM Version:</div>
                        <div class="monitor-stat-value">${systemMetrics.jvmVersion}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Uptime:</div>
                        <div class="monitor-stat-value">${systemMetrics.uptime}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Start Time:</div>
                        <div class="monitor-stat-value">${systemMetrics.startTime}</div>
                    </div>
                </div>
                
                <!-- Application Metrics -->
                <div class="monitor-card">
                    <h3>Application Metrics</h3>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Total Users:</div>
                        <div class="monitor-stat-value">${appMetrics.userCount}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Total Courses:</div>
                        <div class="monitor-stat-value">${appMetrics.courseCount}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Total Quizzes:</div>
                        <div class="monitor-stat-value">${appMetrics.quizCount}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Total Submissions:</div>
                        <div class="monitor-stat-value">${appMetrics.submissionCount}</div>
                    </div>
                    <div class="monitor-stat">
                        <div class="monitor-stat-label">Active Sessions:</div>
                        <div class="monitor-stat-value">${appMetrics.activeSessions}</div>
                    </div>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h2>System Actions</h2>
                </div>
                <div class="card-body">
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/admin/system/logs" class="btn btn-primary">View System Logs</a>
                        <a href="${pageContext.request.contextPath}/admin/system/settings" class="btn btn-secondary">System Settings</a>
                        <button class="btn btn-danger" onclick="confirmGarbageCollection()">Run Garbage Collection</button>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <!-- Confirmation Modal -->
    <div id="gcModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Confirm Action</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to run garbage collection?</p>
                <p>This will temporarily pause the JVM and may affect system performance.</p>
            </div>
            <div class="modal-footer">
                <button id="cancelGC" class="btn btn-secondary">Cancel</button>
                <button id="confirmGC" class="btn btn-danger">Run GC</button>
            </div>
        </div>
    </div>
    
    <script>
        let refreshIntervalId = null;
        
        function refreshPage() {
            location.reload();
        }
        
        function toggleAutoRefresh() {
            const autoRefreshCheckbox = document.getElementById('autoRefresh');
            const refreshIntervalSelect = document.getElementById('refreshInterval');
            
            if (autoRefreshCheckbox.checked) {
                refreshIntervalSelect.disabled = false;
                startAutoRefresh();
            } else {
                refreshIntervalSelect.disabled = true;
                stopAutoRefresh();
            }
        }
        
        function startAutoRefresh() {
            const interval = parseInt(document.getElementById('refreshInterval').value);
            refreshIntervalId = setInterval(refreshPage, interval);
        }
        
        function stopAutoRefresh() {
            if (refreshIntervalId) {
                clearInterval(refreshIntervalId);
                refreshIntervalId = null;
            }
        }
        
        function updateRefreshInterval() {
            stopAutoRefresh();
            startAutoRefresh();
        }
        
        function confirmGarbageCollection() {
            const modal = document.getElementById('gcModal');
            modal.style.display = 'block';
        }
        
        // Modal event handlers
        const modal = document.getElementById('gcModal');
        const closeBtn = document.getElementsByClassName('close')[0];
        const cancelBtn = document.getElementById('cancelGC');
        const confirmBtn = document.getElementById('confirmGC');
        
        closeBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        cancelBtn.onclick = function() {
            modal.style.display = 'none';
        }
        
        confirmBtn.onclick = function() {
            // Send AJAX request to run GC
            fetch('${pageContext.request.contextPath}/admin/system/gc', {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    alert('Garbage collection completed successfully.');
                    refreshPage();
                } else {
                    alert('Error running garbage collection.');
                }
            })
            .catch(error => {
                alert('Error: ' + error);
            })
            .finally(() => {
                modal.style.display = 'none';
            });
        }
        
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    </script>
</body>
</html>