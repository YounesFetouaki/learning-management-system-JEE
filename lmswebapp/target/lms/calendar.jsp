<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calendar - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <!-- FullCalendar CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.css">
    <style>
        .calendar-container {
            background-color: #fff;
            border-radius: 0.25rem;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .calendar-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        
        .calendar-actions {
            display: flex;
            gap: 0.5rem;
        }
        
        .event-form {
            background-color: #f9f9f9;
            border-radius: 0.25rem;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }
        
        .event-form h2 {
            margin-top: 0;
        }
        
        .color-picker {
            display: flex;
            gap: 0.5rem;
            margin-top: 0.5rem;
        }
        
        .color-option {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            cursor: pointer;
            border: 2px solid transparent;
        }
        
        .color-option.selected {
            border-color: #333;
        }
        
        .event-legend {
            margin-top: 1.5rem;
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .legend-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .legend-color {
            width: 15px;
            height: 15px;
            border-radius: 50%;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="includes/header.jsp" />
        
        <div class="content">
            <div class="calendar-header">
                <h1>Calendar</h1>
                <div class="calendar-actions">
                    <button id="addEventBtn" class="btn btn-primary">Add Event</button>
                    <a href="${pageContext.request.contextPath}/calendar/sync" class="btn btn-secondary">Sync Calendar</a>
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
            
            <div id="eventForm" class="event-form" style="display: none;">
                <h2>Add New Event</h2>
                <form action="${pageContext.request.contextPath}/calendar" method="post">
                    <div class="form-group">
                        <label for="title">Event Title:</label>
                        <input type="text" id="title" name="title" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description" rows="3"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="startDate">Start Date and Time:</label>
                        <input type="datetime-local" id="startDate" name="startDate" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="endDate">End Date and Time:</label>
                        <input type="datetime-local" id="endDate" name="endDate">
                    </div>
                    
                    <div class="form-group">
                        <label class="checkbox-label">
                            <input type="checkbox" id="allDay" name="allDay">
                            All Day Event
                        </label>
                    </div>
                    
                    <div class="form-group">
                        <label>Event Color:</label>
                        <input type="hidden" id="color" name="color" value="#3788d8">
                        <div class="color-picker">
                            <div class="color-option selected" data-color="#3788d8" style="background-color: #3788d8;"></div>
                            <div class="color-option" data-color="#4CAF50" style="background-color: #4CAF50;"></div>
                            <div class="color-option" data-color="#F44336" style="background-color: #F44336;"></div>
                            <div class="color-option" data-color="#FF9800" style="background-color: #FF9800;"></div>
                            <div class="color-option" data-color="#9C27B0" style="background-color: #9C27B0;"></div>
                            <div class="color-option" data-color="#795548" style="background-color: #795548;"></div>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Event</button>
                        <button type="button" id="cancelEventBtn" class="btn btn-secondary">Cancel</button>
                    </div>
                </form>
            </div>
            
            <div class="calendar-container">
                <div id="calendar"></div>
            </div>
            
            <div class="event-legend">
                <div class="legend-item">
                    <div class="legend-color" style="background-color: #3788d8;"></div>
                    <span>Personal Event</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color" style="background-color: #4CAF50;"></div>
                    <span>Course Start</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color" style="background-color: #F44336;"></div>
                    <span>Course End</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color" style="background-color: #2196F3;"></div>
                    <span>Quiz Start</span>
                </div>
                <div class="legend-item">
                    <div class="legend-color" style="background-color: #FF9800;"></div>
                    <span>Quiz Due</span>
                </div>
            </div>
        </div>
        
        <jsp:include page="includes/footer.jsp" />
    </div>
    
    <!-- FullCalendar JS -->
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize FullCalendar
            const calendarEl = document.getElementById('calendar');
            const calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
                },
                events: '${pageContext.request.contextPath}/calendar/events',
                editable: true,
                selectable: true,
                selectMirror: true,
                dayMaxEvents: true,
                eventTimeFormat: {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: true
                },
                select: function(info) {
                    // Show event form with selected dates
                    document.getElementById('eventForm').style.display = 'block';
                    document.getElementById('startDate').value = formatDateForInput(info.start);
                    if (info.end) {
                        document.getElementById('endDate').value = formatDateForInput(info.end);
                    }
                    
                    // Scroll to form
                    document.getElementById('eventForm').scrollIntoView({ behavior: 'smooth' });
                },
                eventClick: function(info) {
                    // Only handle personal events for deletion
                    if (!info.event.url) {
                        if (confirm('Delete this event?')) {
                            // Send AJAX request to delete the event
                            fetch('${pageContext.request.contextPath}/calendar/delete', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'eventId=' + info.event.id
                            })
                            .then(response => {
                                if (response.ok) {
                                    info.event.remove();
                                } else {
                                    alert('Error deleting event');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('Error deleting event');
                            });
                        }
                        return false; // Prevent URL navigation
                    }
                }
            });
            calendar.render();
            
            // Event form toggle
            document.getElementById('addEventBtn').addEventListener('click', function() {
                const eventForm = document.getElementById('eventForm');
                eventForm.style.display = eventForm.style.display === 'none' ? 'block' : 'none';
                
                if (eventForm.style.display === 'block') {
                    // Set default dates
                    const now = new Date();
                    document.getElementById('startDate').value = formatDateForInput(now);
                    
                    // Scroll to form
                    eventForm.scrollIntoView({ behavior: 'smooth' });
                }
            });
            
            document.getElementById('cancelEventBtn').addEventListener('click', function() {
                document.getElementById('eventForm').style.display = 'none';
            });
            
            // Color picker
            const colorOptions = document.querySelectorAll('.color-option');
            colorOptions.forEach(option => {
                option.addEventListener('click', function() {
                    // Remove selected class from all options
                    colorOptions.forEach(opt => opt.classList.remove('selected'));
                    // Add selected class to clicked option
                    this.classList.add('selected');
                    // Update hidden input value
                    document.getElementById('color').value = this.getAttribute('data-color');
                });
            });
            
            // All day checkbox
            document.getElementById('allDay').addEventListener('change', function() {
                const startDateInput = document.getElementById('startDate');
                const endDateInput = document.getElementById('endDate');
                
                if (this.checked) {
                    // Store current time values
                    startDateInput.setAttribute('data-time', startDateInput.value.split('T')[1] || '');
                    if (endDateInput.value) {
                        endDateInput.setAttribute('data-time', endDateInput.value.split('T')[1] || '');
                    }
                    
                    // Set time to 00:00
                    if (startDateInput.value) {
                        startDateInput.value = startDateInput.value.split('T')[0] + 'T00:00';
                    }
                    if (endDateInput.value) {
                        endDateInput.value = endDateInput.value.split('T')[0] + 'T23:59';
                    }
                } else {
                    // Restore time values
                    if (startDateInput.value && startDateInput.hasAttribute('data-time')) {
                        startDateInput.value = startDateInput.value.split('T')[0] + 'T' + startDateInput.getAttribute('data-time');
                    }
                    if (endDateInput.value && endDateInput.hasAttribute('data-time')) {
                        endDateInput.value = endDateInput.value.split('T')[0] + 'T' + endDateInput.getAttribute('data-time');
                    }
                }
            });
            
            // Helper function to format date for datetime-local input
            function formatDateForInput(date) {
                const d = new Date(date);
                const year = d.getFullYear();
                const month = String(d.getMonth() + 1).padStart(2, '0');
                const day = String(d.getDate()).padStart(2, '0');
                const hours = String(d.getHours()).padStart(2, '0');
                const minutes = String(d.getMinutes()).padStart(2, '0');
                
                return `${year}-${month}-${day}T${hours}:${minutes}`;
            }
        });
    </script>
</body>
</html>