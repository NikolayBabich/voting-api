INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');


INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);


INSERT INTO restaurants (name, address)
VALUES ('Тройка', 'Загородный пр., 27/21'),
       ('Usoff', 'наб. Лейтенанта Шмидта, 19'),
       ('Палкинъ', 'Невский пр., 47');


INSERT INTO menus (actual_date, restaurant_id)
VALUES (dateadd('day', -3, current_date), 1),
       (dateadd('day', -3, current_date), 2),
       (dateadd('day', -3, current_date), 3),

       (dateadd('day', -2, current_date), 1),
       (dateadd('day', -2, current_date), 2),
       (dateadd('day', -2, current_date), 3),

       (dateadd('day', -1, current_date), 1),
       (dateadd('day', -1, current_date), 2),
       (dateadd('day', -1, current_date), 3),

       (current_date, 1),
       (current_date, 2),
       (current_date, 3);


INSERT INTO dishes (name, price)
VALUES ('Утка по-пекински', 1200),
       ('Фуа-гра', 4500),
       ('Паста болоньезе', 650),
       ('Котлета по-киевски', 750),
       ('Борщ украинский', 600),
       ('Суп гороховый', 500),
       ('Щи малосольные', 400),
       ('Плов из говядины', 950),
       ('Солянка по-домашнему', 750),
       ('Щи малосольные', 350),
       ('Люля-кебаб', 900);


INSERT INTO menu_dishes (menu_id, dish_id)
VALUES ( 1,  1), ( 1,  2), ( 1,  3),
       ( 2,  4), ( 2,  5),
       ( 3,  6), ( 3,  7), ( 3,  8),

       ( 4,  1), ( 4,  2), ( 4,  3),
       ( 5,  4), ( 5,  5),
                 ( 6,  7), ( 6,  8),

       ( 7,  1), ( 7,  2), ( 7,  3),
       ( 8,  4), ( 8,  5), ( 8,  9),
                 ( 9, 10), ( 9,  8), ( 9, 11),

       (10,  1),           (10,  3),
       (11,  4), (11,  5), (11,  9),
       (12,  6), (12, 10), (12,  8), (12, 11);


INSERT INTO votes (user_id, restaurant_id, actual_date)
VALUES (1, 1, dateadd('day', -3, current_date)),
       (2, 2, dateadd('day', -3, current_date)),

       (1, 1, dateadd('day', -2, current_date)),
       (2, 1, dateadd('day', -2, current_date)),

       (1, 3, dateadd('day', -1, current_date)),

       (2, 2, current_date);