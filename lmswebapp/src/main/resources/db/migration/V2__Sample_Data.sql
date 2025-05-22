-- Insert Users (1 admin, 3 teachers, 10 students)
-- Note: In a real application, passwords would be properly hashed
INSERT INTO users (first_name, last_name, email, password_hash, role, active, created_at) VALUES
('Younes', 'Fetouaki', 'younesfetouaki65@gmail.com', 'admin123', 'admin', 1, NOW()),
('Sara', 'Smith', 'sara@lms.com', 'teacher123', 'teacher', 1, NOW()),
('Ahmed', 'Ziani', 'ahmed@lms.com', 'teacher123', 'teacher', 1, NOW()),
('Linda', 'Khan', 'linda@lms.com', 'teacher123', 'teacher', 1, NOW()),
('Student1', 'Alpha', 'student1@lms.com', 'student123', 'student', 1, NOW()),
('Student2', 'Beta', 'student2@lms.com', 'student123', 'student', 1, NOW()),
('Student3', 'Gamma', 'student3@lms.com', 'student123', 'student', 1, NOW()),
('Student4', 'Delta', 'student4@lms.com', 'student123', 'student', 1, NOW()),
('Student5', 'Epsilon', 'student5@lms.com', 'student123', 'student', 1, NOW()),
('Student6', 'Zeta', 'student6@lms.com', 'student123', 'student', 1, NOW()),
('Student7', 'Eta', 'student7@lms.com', 'student123', 'student', 1, NOW()),
('Student8', 'Theta', 'student8@lms.com', 'student123', 'student', 1, NOW()),
('Student9', 'Iota', 'student9@lms.com', 'student123', 'student', 1, NOW()),
('Student10', 'Kappa', 'student10@lms.com', 'student123', 'student', 1, NOW());
