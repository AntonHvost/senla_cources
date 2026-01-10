INSERT INTO product (model, maker, type) VALUES
('A100', 'A', 'PC'),
('A101', 'A', 'PC'),
('A102', 'A', 'PC'),
('A200', 'A', 'Printer'),

('B100', 'B', 'PC'),
('B101', 'B', 'PC'),
('B200', 'B', 'Laptop'),
('B201', 'B', 'Laptop'),
('B202', 'B', 'Laptop'),
('B300', 'B', 'Printer'),

('C100', 'C', 'PC'),
('C101', 'C', 'PC'),

('D200', 'D', 'Laptop'),
('D201', 'D', 'Laptop'),

('E100', 'E', 'PC'),
('E300', 'E', 'Printer'),

('F300', 'F', 'Printer');

INSERT INTO pc (model, speed, ram, hd, cd, price) VALUES
('A100', 450, 64, 8.0, '12x', 450.00),
('B100', 500, 64, 10.0, '12x', 480.00),
('A101', 600, 128, 20.0, '24x', 550.00),
('A102', 800, 256, 40.0, '48x', 950.00),
('B101', 900, 512, 80.0, '48x', 1200.00),
('C100', 450, 64, 10.0, '12x', 400.00),
('C101', 1000, 1024, 160.0, '52x', 1500.00),
('E100', 1200, 64, 20.0, '24x', 600.00);

INSERT INTO laptop (model, speed, ram, hd, price, screen) VALUES
('B200', 750, 256, 30.0, 1100.00, 14),
('B201', 800, 512, 60.0, 1600.00, 15),
('D200', 600, 128, 20.0, 900.00, 13),
('D201', 700, 256, 120.0, 1300.00, 16);

INSERT INTO printer (model, color, type, price) VALUES
('A200', 'y', 'InkJet', 200.00),
('B300', 'y', 'Laser', 300.00),
('F300', 'y', 'Laser', 500.00),
('E300', 'n', 'Laser', 250.00);