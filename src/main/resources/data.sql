-- Seed admin and manager users only if they do not already exist.
-- If a village with code '0101010101' exists it will be used; otherwise village_id will be NULL.

INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, village_id)
SELECT 'admin',
       'admin@theatresystem.com',
       '$2a$10$X2gBqJai2hXeEFwI877nIO1Ivkugef2QK8JvWAA3fCrDJan7WZuvC',
       'System',
       'Administrator',
       '123-456-7890',
       'ROLE_ADMIN',
       (SELECT id FROM villages WHERE code = '0101010101' LIMIT 1)
FROM (SELECT 1) tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, village_id)
SELECT 'manager',
       'manager@theatresystem.com',
       '$2a$10$dR0qs1.LqQs7mNdDuVMx3.fz2nGGEVA2zLnsHlTcItQqnJz/u.YNG',
       'Theatre',
       'Manager',
       '123-456-7891',
       'ROLE_MANAGER',
       (SELECT id FROM villages WHERE code = '0101010101' LIMIT 1)
FROM (SELECT 1) tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'manager');