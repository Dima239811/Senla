INSERT INTO product (model, maker, type) VALUES
('M100', 'A', 'PC'),
('M101', 'A', 'PC'),
('M140', 'C', 'PC'),
('M150', 'C', 'PC'),
('L200', 'A', 'Laptop'),
('L201', 'B', 'Laptop'),
('P300', 'A', 'Printer'),
('P301', 'B', 'Printer'),
('P302', 'C', 'Printer');

INSERT INTO pc (code, model, speed, ram, hd, cd, price) VALUES
(1, 'M100', 800, 1024, 120, '12x', 450.00),
(2, 'M101', 460, 2048, 200, '24x', 550.00),
(3, 'M100', 400, 1024, 120, '8x', 480.00),
(4, 'M100', 400, 1024, 120, '12x', 750.00)
(5, 'M140', 500, 4096, 500, '24x', 800.00),
(6, 'M150', 550, 8192, 1000, '48x', 900.00);


INSERT INTO laptop (code, model, speed, ram, hd, screen, price) VALUES
(10, 'L200', 800, 2048, 150, 15, 1200.00),
(11, 'L201', 300, 1024, 80, 14, 900.00),
(12, 'L200', 350, 2048, 150, 15, 1200.00);

INSERT INTO printer (code, model, color, type, price) VALUES
(20, 'P300', 'y', 'Laser', 200.00),
(21, 'P301', 'n', 'Jet', 180.00),
(22, 'P302', 'y', 'Matrix', 220.00);