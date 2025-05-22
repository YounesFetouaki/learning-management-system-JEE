<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer>
    <div class="footer-container">
        <p>&copy; <%= new java.util.Date().getYear() + 1900 %> Learning Management System. All rights reserved.</p>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/help">Help</a>
            <a href="${pageContext.request.contextPath}/privacy">Privacy Policy</a>
            <a href="${pageContext.request.contextPath}/terms">Terms of Service</a>
        </div>
    </div>
</footer>