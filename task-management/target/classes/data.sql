INSERT INTO users (username, email, password, role)
VALUES
('ahmed', 'ahmed@test.com', '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('ali',  'ali@test.com',  '$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_USER'),
('admin','admin@test.com','$2a$10$64TYtQDugJN1D73cc6YvgeAAb8nCLDOC2qd7eBMvbWhLzc.GwGVJ2', 'ROLE_ADMIN');




INSERT INTO tasks (title, description, status, created_at, user_id)
VALUES
('Study Java', 'Watch basic tutorials', 'NEW', CURRENT_DATE, 1),
('Clean Room', 'Vacuum and organize', 'DONE', CURRENT_DATE, 1),
('Gym Workout', 'Leg day session', 'IN_PROGRESS', CURRENT_DATE, 2);
