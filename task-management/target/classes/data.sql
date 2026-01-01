INSERT INTO users (username, email, password, role)
VALUES
('mohammed', 'mohammed@test.com', '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('fatima',   'fatima@test.com',   '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('omar',     'omar@test.com',     '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('noura',    'noura@test.com',    '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('saleh',    'saleh@test.com',    '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('layan',    'layan@test.com',    '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('admin',    'admin@test.com',    '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_ADMIN');



INSERT INTO tasks (title, description, status, created_at, user_id)
VALUES
('Finish Assignment', 'Complete Java homework', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'mohammed')),

('Attend Lecture', 'Spring Boot session', 'IN_PROGRESS', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'mohammed')),

('Prepare Presentation', 'Slides for project', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'fatima')),

('Email Instructor', 'Ask about deadline', 'DONE', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'fatima')),

('Gym Session', 'Upper body workout', 'IN_PROGRESS', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'omar')),

('Read Book', 'Clean Code chapter 3', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'omar')),

('Team Meeting', 'Discuss project tasks', 'DONE', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'noura')),

('Write Report', 'Weekly progress report', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'noura')),

('Fix Bugs', 'Resolve frontend issues', 'IN_PROGRESS', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'saleh')),

('Push Code', 'Upload changes to GitHub', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'saleh')),

('Design UI', 'Improve task page UI', 'NEW', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'layan')),

('Test System', 'Check user permissions', 'DONE', CURRENT_DATE,
 (SELECT id FROM users WHERE username = 'layan'));

