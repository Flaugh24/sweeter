INSERT INTO usr(active, password, username)
VALUES (TRUE, '$2a$10$DbSW9q.z18FjQ1fDqQ3.b.M95OL8Ddwb0Xe8RieaH4KeDoF10LRqS', 'admin');

INSERT INTO user_role(user_id, roles)
VALUES (1, 'ADMIN'),
       (1, 'USER');

SELECT setval('usr_id_seq', (SELECT MAX(id) FROM usr));