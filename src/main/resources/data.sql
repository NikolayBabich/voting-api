-- noinspection SpellCheckingInspectionForFile

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{bcrypt}$2y$12$A04VJbvNauYf5HYhSSOC5OHqFTSgwC18PtVJXW3xBfgKfZf6oBsNa'),
       ('Admin', 'admin@gmail.com', '{bcrypt}$2a$10$I5tyU0JNsRtxtiMXUJfluePuM4V3XBux9297q3Xe0QdGqj1dcY/U2'),
       ('Gourmet', 'gourmet@toprest.fr', '{bcrypt}$2a$10$IzkPp0f6iEZoMkTQTS6TJeGsjcg.kW0WulBoYtlQDPtp6eS7TmFEO');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO restaurant (name, address)
VALUES ('Тройка', 'Загородный пр., 27/21'),
       ('Usoff', 'наб. Лейтенанта Шмидта, 19'),
       ('Мари Vanna', 'Мытнинская наб., 3'),
       ('Палкинъ', 'Невский пр., 47');

INSERT INTO menu (lunch_date, restaurant_id)
VALUES (dateadd('day', -4, current_date), 1),
       (dateadd('day', -4, current_date), 2),
       (dateadd('day', -4, current_date), 3),
       (dateadd('day', -4, current_date), 4),

       (dateadd('day', -3, current_date), 1),
       (dateadd('day', -3, current_date), 2),
       (dateadd('day', -3, current_date), 3),
       (dateadd('day', -3, current_date), 4),

       (dateadd('day', -2, current_date), 1),
       (dateadd('day', -2, current_date), 2),
       (dateadd('day', -2, current_date), 3),
       (dateadd('day', -2, current_date), 4),

       (dateadd('day', -1, current_date), 1),
       (dateadd('day', -1, current_date), 2),
       (dateadd('day', -1, current_date), 3),
       (dateadd('day', -1, current_date), 4),

       (current_date, 1),
       (current_date, 2),
       (current_date, 3),
       (current_date, 4);


INSERT INTO dish (name, price)
VALUES ('Утка по-пекински', 1200),
       ('Котлета по-киевски', 750),
       ('Борщ украинский', 600),
       ('Фуа-гра', 4500),
       ('Паста болоньезе', 650),
       ('Суп гороховый', 500),
       ('Щи малосольные', 400),
       ('Люля-кебаб', 900);


INSERT INTO menu_dish (menu_id, dish_id)
VALUES (1, 1), (1, 2), (1, 3),
       (2, 3), (2, 4),
       (3, 6), (3, 7), (3, 8),
       (4, 4), (4, 5),

       (5, 1), (5, 3),
       (6, 3), (6, 4), (6, 5),
       (7, 6), (7, 7), (7, 8),
       (8, 4), (8, 5), (8, 1),

       (9, 1), (9, 2), (9, 3),
       (10, 3), (10, 4),
       (11, 6), (11, 7), (11, 8),
       (12, 4), (12, 5),

       (13, 1), (13, 2), (13, 3),
       (14, 3), (14, 4),
       (15, 6), (15, 7), (15, 8),
       (16, 4), (16, 5),

       (17, 1), (17, 3),
       (18, 3), (18, 7),
       (19, 6), (19, 7), (19, 8),
       (20, 4), (20, 5), (20, 1);


INSERT INTO vote (user_id, restaurant_id, lunch_date)
VALUES (1, 3, dateadd('day', -4, current_date)),
       (2, 1, dateadd('day', -4, current_date)),

       (1, 3, dateadd('day', -3, current_date)),
       (2, 2, dateadd('day', -3, current_date)),
       (3, 4, dateadd('day', -3, current_date)),

       (1, 2, dateadd('day', -2, current_date)),
       (3, 4, dateadd('day', -2, current_date)),

       (1, 1, dateadd('day', -1, current_date)),
       (2, 1, dateadd('day', -1, current_date)),
       (3, 4, dateadd('day', -1, current_date)),

       (3, 3, current_date);
