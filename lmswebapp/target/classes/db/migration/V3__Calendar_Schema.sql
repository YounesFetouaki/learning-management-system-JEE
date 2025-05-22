-- Calendar events table
CREATE TABLE calendar_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NULL,
    event_type ENUM('COURSE_START', 'COURSE_END', 'QUIZ_START', 'QUIZ_END', 'ASSIGNMENT_DUE', 'PERSONAL') NOT NULL,
    all_day BOOLEAN NOT NULL DEFAULT FALSE,
    color VARCHAR(7) NULL,
    user_id BIGINT NULL,
    course_id BIGINT NULL,
    quiz_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Create some initial calendar events
-- Course start/end events
INSERT INTO calendar_events (title, description, start_date, end_date, event_type, all_day, color, course_id)
SELECT 
    CONCAT('Course Start: ', title),
    CONCAT('Start of the course: ', title),
    start_date,
    start_date,
    'COURSE_START',
    TRUE,
    '#4CAF50',
    id
FROM courses
WHERE start_date IS NOT NULL;

INSERT INTO calendar_events (title, description, start_date, end_date, event_type, all_day, color, course_id)
SELECT 
    CONCAT('Course End: ', title),
    CONCAT('End of the course: ', title),
    end_date,
    end_date,
    'COURSE_END',
    TRUE,
    '#F44336',
    id
FROM courses
WHERE end_date IS NOT NULL;

-- Quiz start/end events
INSERT INTO calendar_events (title, description, start_date, end_date, event_type, all_day, color, course_id, quiz_id)
SELECT 
    CONCAT('Quiz Start: ', q.title),
    CONCAT('Start of the quiz: ', q.title, ' for course: ', c.title),
    q.start_date,
    q.start_date,
    'QUIZ_START',
    TRUE,
    '#2196F3',
    q.course_id,
    q.id
FROM quizzes q
JOIN courses c ON q.course_id = c.id
WHERE q.start_date IS NOT NULL;

INSERT INTO calendar_events (title, description, start_date, end_date, event_type, all_day, color, course_id, quiz_id)
SELECT 
    CONCAT('Quiz Due: ', q.title),
    CONCAT('Due date for the quiz: ', q.title, ' for course: ', c.title),
    q.end_date,
    q.end_date,
    'QUIZ_END',
    TRUE,
    '#FF9800',
    q.course_id,
    q.id
FROM quizzes q
JOIN courses c ON q.course_id = c.id
WHERE q.end_date IS NOT NULL;