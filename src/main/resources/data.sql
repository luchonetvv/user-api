-- USER
-- hashed password: nosecuales
INSERT INTO USER (id, name, email, password, last_login, token, is_active, created, modified) VALUES
('1cb2a08a-1bca-479d-9e3e-15b869cda8c5',  'Luis Guido Calderon', 'lucho@gmail.com', '$2a$10$yUsFs6zgMWsR.iKFtvl9wO.ltm/HwVeTNN54r.l.BX6Eg8RpYaqhC', '2021-09-24T15:28:03.123456789', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsdWNob0BnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjMyNzE1NjQ0LCJleHAiOjE2MzI3MTYyNDR9.tCqhdF_u9fsr-C9YMgfM8J-bcL-CJTIgDzuvpNI5MII', true, '2021-09-24T15:28:03.123456789', null);

-- PHONES
INSERT INTO PHONE (id, user_id, number, city_code, country_code) VALUES
('1793d30f-8a77-46bc-8100-1469b48b9c28', '1cb2a08a-1bca-479d-9e3e-15b869cda8c5', '84990165', 'MNG', 'NIC'),
('71f83f32-101b-42a2-b3a2-efb6a903ffad', '1cb2a08a-1bca-479d-9e3e-15b869cda8c5', '85778128', 'MNG', 'NIC');

-- ROLES
INSERT INTO USRROLE (id, role_name, description) VALUES (1, 'ROLE_ADMIN', 'Administrator');

-- USER WITH ROLE
INSERT INTO USER_ROLE(id, user_id, usrrole_id) VALUES (1, '1cb2a08a-1bca-479d-9e3e-15b869cda8c5', 1); -- give admin ROLE_ADMIN
