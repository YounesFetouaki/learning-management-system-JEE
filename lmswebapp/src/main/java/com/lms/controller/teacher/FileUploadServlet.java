package com.lms.controller.teacher;

import com.lms.dao.CourseDAO;
import com.lms.dao.FileResourceDAO;
import com.lms.model.Course;
import com.lms.model.FileResource;
import com.lms.model.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/teacher/files/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 10 * 1024 * 1024,  // 10 MB
    maxRequestSize = 50 * 1024 * 1024 // 50 MB
)
public class FileUploadServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final CourseDAO courseDAO = new CourseDAO();
    private final FileResourceDAO fileResourceDAO = new FileResourceDAO();
    private static final String UPLOAD_DIRECTORY = "course_files";
    
    @Override
    public void init() throws ServletException {
        // Create upload directory if it doesn't exist
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all courses for file management
            List<Course> teacherCourses = courseDAO.findByTeacher(teacher);
            request.setAttribute("courses", teacherCourses);
            
            // Get course ID from request parameter if provided
            String courseIdParam = request.getParameter("courseId");
            if (courseIdParam != null && !courseIdParam.isEmpty()) {
                try {
                    Long courseId = Long.parseLong(courseIdParam);
                    Course selectedCourse = null;
                    
                    // Verify the course belongs to the teacher
                    for (Course course : teacherCourses) {
                        if (course.getId().equals(courseId)) {
                            selectedCourse = course;
                            break;
                        }
                    }
                    
                    if (selectedCourse != null) {
                        List<FileResource> files = fileResourceDAO.findByCourse(selectedCourse);
                        request.setAttribute("selectedCourse", selectedCourse);
                        request.setAttribute("files", files);
                    }
                } catch (NumberFormatException e) {
                    // Invalid course ID, ignore
                }
            }
            
            request.getRequestDispatcher("/teacher/files.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/download/")) {
            // Handle file download
            try {
                Long fileId = Long.parseLong(pathInfo.substring(10));
                FileResource file = fileResourceDAO.findById(fileId);
                
                if (file != null && file.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    // Set content type and headers
                    response.setContentType(file.getContentType());
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
                    
                    // Get file path
                    String filePath = getServletContext().getRealPath("") + File.separator + 
                                     UPLOAD_DIRECTORY + File.separator + file.getStoredFileName();
                    
                    // Stream file to response
                    Files.copy(Paths.get(filePath), response.getOutputStream());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to download this file");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User teacher = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Handle file upload
            String courseIdStr = request.getParameter("courseId");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart("file");
            
            // Validate input
            if (courseIdStr == null || courseIdStr.trim().isEmpty() ||
                title == null || title.trim().isEmpty() ||
                filePart == null || filePart.getSize() == 0) {
                
                request.setAttribute("error", "Course, title, and file are required");
                doGet(request, response);
                return;
            }
            
            try {
                Long courseId = Long.parseLong(courseIdStr);
                Course course = courseDAO.findById(courseId);
                
                // Verify the course belongs to the teacher
                if (course == null || !course.getTeacher().getId().equals(teacher.getId())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to upload files to this course");
                    return;
                }
                
                // Get file details
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String contentType = filePart.getContentType();
                long fileSize = filePart.getSize();
                
                // Generate unique stored file name
                String storedFileName = UUID.randomUUID().toString() + "_" + fileName;
                
                // Save file to disk
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                Path filePath = Paths.get(uploadPath, storedFileName);
                filePart.write(filePath.toString());
                
                // Create file resource in database
                FileResource fileResource = new FileResource();
                fileResource.setCourse(course);
                fileResource.setTitle(title);
                fileResource.setDescription(description);
                fileResource.setFileName(fileName);
                fileResource.setStoredFileName(storedFileName);
                fileResource.setContentType(contentType);
                fileResource.setFileSize(fileSize);
                fileResource.setUploadDate(new Date());
                fileResource.setUploadedBy(teacher);
                
                fileResourceDAO.create(fileResource);
                
                // Redirect back to files page
                response.sendRedirect(request.getContextPath() + "/teacher/files?courseId=" + courseId);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid course ID");
                doGet(request, response);
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Handle file deletion
            try {
                Long fileId = Long.parseLong(pathInfo.substring(8));
                FileResource file = fileResourceDAO.findById(fileId);
                
                if (file != null && file.getCourse().getTeacher().getId().equals(teacher.getId())) {
                    // Delete file from disk
                    String filePath = getServletContext().getRealPath("") + File.separator + 
                                     UPLOAD_DIRECTORY + File.separator + file.getStoredFileName();
                    Files.deleteIfExists(Paths.get(filePath));
                    
                    // Delete file record from database
                    Long courseId = file.getCourse().getId();
                    fileResourceDAO.delete(file);
                    
                    // Redirect back to files page
                    response.sendRedirect(request.getContextPath() + "/teacher/files?courseId=" + courseId);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to delete this file");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}