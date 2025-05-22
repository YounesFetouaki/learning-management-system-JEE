package com.lms.controller.auth;

import com.lms.model.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get the requested URI
        String uri = httpRequest.getRequestURI();
        
        // Skip filter for login page, static resources, etc.
        if (isResourcePublic(uri)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        // Check role-based access
        User user = (User) session.getAttribute("user");
        if (!hasAccess(user, uri)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code
    }
    
    private boolean isResourcePublic(String uri) {
        return uri.endsWith("login") || 
               uri.endsWith("logout") || 
               uri.contains("/css/") || 
               uri.contains("/js/") || 
               uri.contains("/images/");
    }
    
    private boolean hasAccess(User user, String uri) {
        // Admin has access to everything
        if (user.getRole() == User.Role.ADMIN) {
            return true;
        }
        
        // Check role-specific paths
        if (uri.contains("/admin/") && user.getRole() != User.Role.ADMIN) {
            return false;
        }
        
        if (uri.contains("/teacher/") && user.getRole() != User.Role.TEACHER) {
            return false;
        }
        
        if (uri.contains("/student/") && user.getRole() != User.Role.STUDENT) {
            return false;
        }
        
        return true;
    }
}