-- Инициализация БД 

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



--- Примеры

-- 1)
SELECT * FROM products, shop_products 
WHERE products.product_id = shop_products.product_id;


-- 2)
SELECT * FROM products 
JOIN shop_products ON products.product_id = shop_products.product_id;


-- 3)
SELECT * FROM products 
JOIN shop_products ON products.product_id = shop_products.product_id 
WHERE shop_products.published = TRUE


-- 4)
SELECT * FROM products, shop_products
WHERE shop_products.published = TRUE
