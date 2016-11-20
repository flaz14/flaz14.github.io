---=== Инициализация БД ===---

DROP SCHEMA join_example IF EXISTS;

CREATE SCHEMA join_example;

SET SCHEMA join_example;

CREATE TABLE shops (
	shop_id INTEGER PRIMARY KEY,
	shop_name VARCHAR
);

CREATE TABLE products (
	product_id INTEGER PRIMARY KEY,
	product_name VARCHAR
);

CREATE TABLE shop_products (
	shop_id INTEGER,
	product_id INTEGER,
	published BOOLEAN,
	FOREIGN KEY (shop_id) REFERENCES shops(shop_id),
	FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO shops (shop_id, shop_name) VALUES (1, 'China');
INSERT INTO shops (shop_id, shop_name) VALUES (2, 'Vietnam');
INSERT INTO shops (shop_id, shop_name) VALUES (3, 'Taiwan');

INSERT INTO products (product_id, product_name) VALUES (1, 'nVidia');
INSERT INTO products (product_id, product_name) VALUES (2, 'AsRock');
INSERT INTO products (product_id, product_name) VALUES (3, 'Asus');

INSERT INTO shop_products (shop_id, product_id, published) VALUES (1, 2, TRUE);
INSERT INTO shop_products (shop_id, product_id, published) VALUES (2, 3, FALSE);
INSERT INTO shop_products (shop_id, product_id, published) VALUES (3, 1, TRUE);
INSERT INTO shop_products (shop_id, product_id, published) VALUES (1, 3, TRUE);



---=== Примеры ===---

-- 1)
-- Выберем все возможные комбинации товаров (т.е. все товары всех поставщиков).
-- Здесь WHERE нужен для того, чтобы ячейки из разных таблиц не путались (SELECT не умеет сопоставлять результаты). 
-- WHERE работает, но непонятно, в каком случае данные из нескольких таблиц нужно собрать воедино, а в каком - 
-- отфильтровать записи по какому-либо критерию.
SELECT * FROM products, shop_products 
WHERE products.product_id = shop_products.product_id;


-- 2)
-- То же, что и в Примере 1, только с помощью "настоящего" JOIN.
-- Здесь уже явно сказано: взять все строки из таблицы products, затем кажду строку сопоставить с каждой строкой из 
-- таблицы shop_products (сопоставить product_id).
SELECT * FROM products 
JOIN shop_products ON products.product_id = shop_products.product_id;


-- 3)
-- Выберем все возможные товары, но только те, что есть в наличии.
-- Работает это так:
--   1. Берем все строки из таблицы products.
--   2. К каждой из выбранных на Шаге 1 строк "приклеиваем" строку из таблицы shop_products, но "склеиваем" только те 
--      строки, у которых одинаковый product_id. На этом этапе получается таблица из 4 строк и 5 столбцов (столбец 
--      product_id встречается в двух изначальных таблицах, но после JOIN остается один экземпляр).
--   3. Из того, что получилось на Шаге 2, выбираем только те строки, в которых поле published = TRUE.
SELECT * FROM products 
JOIN shop_products ON products.product_id = shop_products.product_id 
WHERE shop_products.published = TRUE


-- 4)
-- Попробуем сделать то же самое, что и в Примере 3, но без JOIN
-- Получится таблица из 9 строк и 6 столбцов. 
-- Почему 9? Потому что, если не указан JOIN (или WHERE table_a.id = table_b.id)
-- он просто перемножает 1-ую таблицу на 2-ую (хрен его знает, как именно перемножает, 
-- но в результате получается "каждый с каждым")
-- 
-- Работает это так:
-- 1. Две таблицы перемножаются. Результат перемножения - таблица из 12 строк и 6 столбцов.
--    (т.е. 12 = 3 x 4, ведь в таблицу products мы вставили 3 записи, а в таблицу shop_products - 4 записи):
--    PRODUCT_ID | PRODUCT_NAME | SHOP_ID | PRODUCT_ID | PUBLISHED  
--    1            nVidia         1         2            TRUE
--    1            nVidia         2	        3            FALSE
--    1	           nVidia         3         1            TRUE
--    1            nVidia         1	        3            TRUE
--    2            AsRock         1         2            TRUE
--    2            AsRock         2         3            FALSE
--    2            AsRock         3         1            TRUE
--    2            AsRock         1         3            TRUE
--    3            Asus           1         2            TRUE
--    3            Asus           2         3            FALSE
--    3            Asus           3         1            TRUE
--    3            Asus           1         3            TRUE
--
-- 2. Из получившейся на шаге 1 таблицы остются только те строки, у которых поле published = TRUE.
--    PRODUCT_ID | PRODUCT_NAME | SHOP_ID | PRODUCT_ID | PUBLISHED  
--    1            nVidia         1         2            TRUE
--    1	           nVidia         3         1            TRUE
--    1            nVidia         1	        3            TRUE
--    2            AsRock         1         2            TRUE
--    2            AsRock         3         1            TRUE
--    2            AsRock         1         3            TRUE
--    3            Asus           1         2            TRUE
--    3            Asus           3         1            TRUE
--    3            Asus           1         3            TRUE
--
-- Тут еще можно заметить, что в результате получилась таблица из 6 столбцов, т.е. 
-- столбец встречается дважды. Может, именно из-за этого в программе возникает ошибка...
SELECT * FROM products, shop_products
WHERE shop_products.published = TRUE




