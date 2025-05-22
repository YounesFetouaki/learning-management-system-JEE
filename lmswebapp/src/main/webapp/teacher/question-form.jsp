<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty question ? 'Add' : 'Edit'} Question - LMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../includes/header.jsp" />
        
        <div class="content">
            <div class="page-header">
                <h1>${empty question ? 'Add' : 'Edit'} Question</h1>
                <div class="page-actions">
                    <c:choose>
                        <c:when test="${empty question}">
                            <a href="${pageContext.request.contextPath}/teacher/questions/quiz/${param.quizId}" class="btn btn-secondary">
                                <i class="icon-arrow-left"></i> Back to Questions
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/teacher/questions/quiz/${question.quiz.id}" class="btn btn-secondary">
                                <i class="icon-arrow-left"></i> Back to Questions
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="error-message">
                    <p>${error}</p>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/teacher/questions${empty question ? '' : '/edit/'.concat(question.id)}" method="post" class="form" id="questionForm">
                        <c:if test="${empty question}">
                            <input type="hidden" name="quizId" value="${param.quizId}">
                        </c:if>
                        
                        <div class="form-group">
                            <label for="text">Question Text</label>
                            <textarea id="text" name="text" rows="3" required>${question.text}</textarea>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="type">Question Type</label>
                                <select id="type" name="type" required onchange="toggleAnswerOptions()">
                                    <option value="">Select Type</option>
                                    <option value="MULTIPLE_CHOICE" ${question.type == 'MULTIPLE_CHOICE' ? 'selected' : ''}>Multiple Choice</option>
                                    <option value="TRUE_FALSE" ${question.type == 'TRUE_FALSE' ? 'selected' : ''}>True/False</option>
                                    <option value="SHORT_ANSWER" ${question.type == 'SHORT_ANSWER' ? 'selected' : ''}>Short Answer</option>
                                    <option value="ESSAY" ${question.type == 'ESSAY' ? 'selected' : ''}>Essay</option>
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="points">Points</label>
                                <input type="number" id="points" name="points" value="${empty question ? '1' : question.points}" min="1" required>
                            </div>
                        </div>
                        
                        <div id="answerOptionsContainer" style="display: none;">
                            <h3>Answer Options</h3>
                            <p class="help-text">Select the correct answer(s) by checking the box next to it.</p>
                            
                            <div id="multipleChoiceOptions" style="display: none;">
                                <div id="answersList">
                                    <c:choose>
                                        <c:when test="${not empty answers}">
                                            <c:forEach items="${answers}" var="answer" varStatus="status">
                                                <div class="answer-option">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-10">
                                                            <input type="text" name="answerText" value="${answer.text}" placeholder="Answer option" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label class="checkbox-container">
                                                                <input type="checkbox" name="correctAnswer" value="${status.index}" ${answer.correct ? 'checked' : ''}>
                                                                <span class="checkmark"></span>
                                                                Correct
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="answer-option">
                                                <div class="form-row">
                                                    <div class="form-group col-md-10">
                                                        <input type="text" name="answerText" placeholder="Answer option" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label class="checkbox-container">
                                                            <input type="checkbox" name="correctAnswer" value="0">
                                                            <span class="checkmark"></span>
                                                            Correct
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="answer-option">
                                                <div class="form-row">
                                                    <div class="form-group col-md-10">
                                                        <input type="text" name="answerText" placeholder="Answer option" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label class="checkbox-container">
                                                            <input type="checkbox" name="correctAnswer" value="1">
                                                            <span class="checkmark"></span>
                                                            Correct
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <button type="button" class="btn btn-secondary" onclick="addAnswerOption()">Add Answer Option</button>
                            </div>
                            
                            <div id="trueFalseOptions" style="display: none;">
                                <div class="answer-option">
                                    <div class="form-row">
                                        <div class="form-group col-md-10">
                                            <input type="text" name="answerText" value="True" readonly>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label class="checkbox-container">
                                                <input type="checkbox" name="correctAnswer" value="0" id="trueOption">
                                                <span class="checkmark"></span>
                                                Correct
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="answer-option">
                                    <div class="form-row">
                                        <div class="form-group col-md-10">
                                            <input type="text" name="answerText" value="False" readonly>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label class="checkbox-container">
                                                <input type="checkbox" name="correctAnswer" value="1" id="falseOption">
                                                <span class="checkmark"></span>
                                                Correct
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">${empty question ? 'Add' : 'Update'} Question</button>
                            <c:choose>
                                <c:when test="${empty question}">
                                    <a href="${pageContext.request.contextPath}/teacher/questions/quiz/${param.quizId}" class="btn btn-secondary">Cancel</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/teacher/questions/quiz/${question.quiz.id}" class="btn btn-secondary">Cancel</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <jsp:include page="../includes/footer.jsp" />
    </div>
    
    <script>
        let answerCount = ${not empty answers ? answers.size() : 2};
        
        function toggleAnswerOptions() {
            const questionType = document.getElementById('type').value;
            const answerOptionsContainer = document.getElementById('answerOptionsContainer');
            const multipleChoiceOptions = document.getElementById('multipleChoiceOptions');
            const trueFalseOptions = document.getElementById('trueFalseOptions');
            
            if (questionType === 'MULTIPLE_CHOICE') {
                answerOptionsContainer.style.display = 'block';
                multipleChoiceOptions.style.display = 'block';
                trueFalseOptions.style.display = 'none';
            } else if (questionType === 'TRUE_FALSE') {
                answerOptionsContainer.style.display = 'block';
                multipleChoiceOptions.style.display = 'none';
                trueFalseOptions.style.display = 'block';
                
                // Ensure only one option is selected
                document.getElementById('trueOption').addEventListener('change', function() {
                    if (this.checked) {
                        document.getElementById('falseOption').checked = false;
                    }
                });
                
                document.getElementById('falseOption').addEventListener('change', function() {
                    if (this.checked) {
                        document.getElementById('trueOption').checked = false;
                    }
                });
                
                // Set default values for true/false if editing
                <c:if test="${not empty answers && question.type == 'TRUE_FALSE'}">
                    <c:forEach items="${answers}" var="answer">
                        if ('${answer.text}' === 'True' && ${answer.correct}) {
                            document.getElementById('trueOption').checked = true;
                        } else if ('${answer.text}' === 'False' && ${answer.correct}) {
                            document.getElementById('falseOption').checked = true;
                        }
                    </c:forEach>
                </c:if>
            } else {
                answerOptionsContainer.style.display = 'none';
            }
        }
        
        function addAnswerOption() {
            const answersList = document.getElementById('answersList');
            const newOption = document.createElement('div');
            newOption.className = 'answer-option';
            newOption.innerHTML = `
                <div class="form-row">
                    <div class="form-group col-md-10">
                        <input type="text" name="answerText" placeholder="Answer option" required>
                    </div>
                    <div class="form-group col-md-2">
                        <label class="checkbox-container">
                            <input type="checkbox" name="correctAnswer" value="${answerCount}">
                            <span class="checkmark"></span>
                            Correct
                        </label>
                    </div>
                </div>
                <button type="button" class="btn btn-sm btn-danger remove-option" onclick="removeAnswerOption(this)">Remove</button>
            `;
            answersList.appendChild(newOption);
            answerCount++;
        }
        
        function removeAnswerOption(button) {
            const answerOption = button.parentNode;
            answerOption.parentNode.removeChild(answerOption);
            
            // Update the values of the remaining options
            const checkboxes = document.querySelectorAll('input[name="correctAnswer"]');
            checkboxes.forEach((checkbox, index) => {
                checkbox.value = index;
            });
            
            answerCount--;
        }
        
        // Form validation
        document.getElementById('questionForm').addEventListener('submit', function(e) {
            const questionType = document.getElementById('type').value;
            
            if (questionType === 'MULTIPLE_CHOICE') {
                const answerInputs = document.querySelectorAll('input[name="answerText"]');
                const correctAnswers = document.querySelectorAll('input[name="correctAnswer"]:checked');
                
                if (answerInputs.length < 2) {
                    e.preventDefault();
                    alert('Multiple choice questions must have at least 2 answer options.');
                    return;
                }
                
                if (correctAnswers.length === 0) {
                    e.preventDefault();
                    alert('Please select at least one correct answer.');
                    return;
                }
            } else if (questionType === 'TRUE_FALSE') {
                const correctAnswers = document.querySelectorAll('input[name="correctAnswer"]:checked');
                
                if (correctAnswers.length !== 1) {
                    e.preventDefault();
                    alert('True/False questions must have exactly one correct answer.');
                    return;
                }
            }
        });
        
        // Initialize the form
        window.onload = function() {
            toggleAnswerOptions();
        };
    </script>
</body>
</html>