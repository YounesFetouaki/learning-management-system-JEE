<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Take Quiz - ${quiz.title}</title>
</head>
<body>
    <h1>${quiz.title}</h1>
    <p>${quiz.description}</p>
    <p>Time limit: ${quiz.timeLimitMinutes} minutes</p>
    
    <form action="${pageContext.request.contextPath}/student/quiz/submit" method="post">
        <input type="hidden" name="quizId" value="${quiz.id}" />
        
        <c:forEach var="question" items="${quiz.questions}">
            <div>
                <p><strong>Q${question.id}:</strong> ${question.text}</p>
                
                <!-- Example for multiple choice question -->
                <c:if test="${question.type == 'MULTIPLE_CHOICE'}">
                    <c:forEach var="option" items="${question.options}">
                        <label>
                            <input type="radio" name="answer_${question.id}" value="${option.id}" />
                            ${option.text}
                        </label><br/>
                    </c:forEach>
                </c:if>
                
                <!-- Example for text question -->
                <c:if test="${question.type == 'TEXT'}">
                    <textarea name="answer_${question.id}" rows="4" cols="50"></textarea>
                </c:if>
            </div>
            <hr/>
        </c:forEach>
        
        <button type="submit">Submit Quiz</button>
    </form>
</body>
</html>
