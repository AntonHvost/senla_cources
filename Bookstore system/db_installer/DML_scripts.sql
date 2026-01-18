TRUNCATE TABLE book_request RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_item RESTART IDENTITY CASCADE;
TRUNCATE TABLE "order" RESTART IDENTITY CASCADE;
TRUNCATE TABLE book RESTART IDENTITY CASCADE;
TRUNCATE TABLE consumer RESTART IDENTITY CASCADE;

INSERT INTO consumer (id, name, phone, email) VALUES
(1, 'Anton', '384743874', 'dkjfkj@gmail.com'),
(2, 'Anya', '9483948', 'kdfjfk@gmail.com'),
(3, 'Maksim', '89533483312', 'kdfjdfk@yandex.ru'),
(4, 'Borya', '84384748', 'fjdfhfj@yandex.ru');

INSERT INTO book (id, title, author, description, publish_date, price, status) VALUES
(1, 'Война и мир', 'Лев Толстой', 'Эпический роман о русском обществе и войне 1812 года', '1869-01-01', 1200.50, 'AVAILABLE'),
(2, 'Преступление и наказание', 'Фёдор Достоевский', 'Роман о молодом студенте, совершившем убийство', '1866-01-01', 950.00, 'AVAILABLE'),
(3, 'Мастер и Маргарита', 'Михаил Булгаков', 'Роман о дьяволе, пришедшем в советскую Москву', '1967-01-01', 1100.75, 'AVAILABLE'),
(4, 'Евгений Онегин', 'Александр Пушкин', 'Роман в стихах', '1833-01-01', 750.25, 'AVAILABLE'),
(5, 'Герой нашего времени', 'Михаил Лермонтов', 'Роман, состоящий из нескольких повестей', '1840-01-01', 680.00, 'OUT_OF_STOCK'),
(6, 'Анна Каренина', 'Лев Толстой', 'Роман о жизни русского дворянства', '1878-01-01', 1050.00, 'AVAILABLE'),
(7, 'Братья Карамазовы', 'Фёдор Достоевский', 'Философский роман о вере, морали и семье', '1880-01-01', 1300.00, 'AVAILABLE'),
(8, '1984', 'Джордж Оруэлл', 'Антиутопия о тоталитарном государстве', '1949-06-08', 850.50, 'AVAILABLE'),
(9, 'Убить пересмешника', 'Харпер Ли', 'Роман о расизме и предрассудках в южном США', '1960-07-11', 900.00, 'AVAILABLE'),
(10, 'Великий Гэтсби', 'Фрэнсис Скотт Фицджеральд', 'Роман о жизни и любви в эпохе джаза', '1925-04-10', 800.25, 'OUT_OF_STOCK');

INSERT INTO "order" (id, consumer_id, created_at, completed_at, total_price, status) VALUES
(1, 1, '2025-12-08 02:10:37', NULL, 17508.75, 'IN_PROCESS'),
(2, 2, '2025-12-08 02:11:10', NULL, 56277.50, 'IN_PROCESS'),
(3, 3, '2025-12-08 02:31:29', NULL, 3001.00, 'NEW'),
(4, 4, '2025-12-08 05:18:51', NULL, 1200.50, 'NEW');

INSERT INTO order_item (id, order_id, book_id, quantity) VALUES
(1, 1, 1, 10),
(2, 1, 3, 5),
(3, 2, 3, 30),
(4, 2, 2, 10),
(5, 2, 6, 5),
(6, 2, 8, 10),
(7, 3, 4, 4),
(8, 4, 1, 1);

INSERT INTO book_request (id, order_id, book_id, create_at, delivery_date, status) VALUES
(1, 1, 3, '2025-12-08 02:10:37', '2025-12-08 05:20:17', 'PENDING'),
(2, 2, 3, '2025-12-08 02:11:10', '2025-12-08 05:20:17', 'PENDING');