-- Create system_logs table for logging system events
CREATE TABLE system_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    level VARCHAR(10) NOT NULL,
    category VARCHAR(50) NOT NULL,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    details TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    
    -- Indexes for better query performance
    INDEX idx_timestamp (timestamp),
    INDEX idx_category (category),
    INDEX idx_level (level),
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    
    -- Foreign key constraint (optional, depends on your setup)
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert some sample log entries
INSERT INTO system_logs (level, category, user_id, action, message, details, timestamp, ip_address) VALUES
('INFO', 'SYSTEM', NULL, 'SYSTEM_START', 'LMS System started successfully', 'Application initialization completed', NOW(), '127.0.0.1'),
('INFO', 'SECURITY', 1, 'LOGIN', 'Admin user logged in', 'Successful authentication', NOW(), '192.168.1.100'),
('WARN', 'SECURITY', NULL, 'FAILED_LOGIN', 'Failed login attempt', 'Invalid credentials for user: admin@example.com', NOW(), '192.168.1.101'),
('INFO', 'USER_ACTIVITY', 2, 'COURSE_ACCESS', 'User accessed course', 'Course ID: 1, Course Name: Introduction to Programming', NOW(), '192.168.1.102'),
('ERROR', 'DATABASE', NULL, 'CONNECTION_ERROR', 'Database connection failed', 'Connection timeout after 30 seconds', NOW(), '127.0.0.1');