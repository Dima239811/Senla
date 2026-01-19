-- 1. Найти номер модели, скорость и размер жесткого диска для всех ПК стоимостью менее 500 долларов.
SELECT model, speed, hd FROM PC WHERE price > CAST(500 AS money);

-- 2. Найти производителей принтеров. Вывести поля: maker.
SELECT maker from product WHERE type='Printer';

-- 3. Найти номер модели, объем памяти и размеры экранов ноутбуков, цена которых превышает 1000 долларов.
SELECT model, ram, screen FROM laptop where price > CAST(1000 as money);

-- 4. Найти все записи таблицы Printer для цветных принтеров.
SELECT * FROM printer where color = 'y';

-- 5. Найти номер модели, скорость и размер жесткого диска для ПК, имеющих скорость cd 12x или 24x и цену менее 600 долларов.
SELECT model, speed, hd FROM PC WHERE (cd = '12x' OR cd = '24x') AND price < CAST(600 as money);

-- 6. Указать производителя и скорость для тех ноутбуков, которые имеют жесткий диск объемом не менее 100 Гбайт.
SELECT maker, speed FROM product
INNER JOIN laptop on product.model = laptop.model WHERE laptop.hd >= 100;

-- 7. Найти номера моделей и цены всех продуктов (любого типа), выпущенных производителем B (латинская буква).
SELECT p.model, pc.price
FROM product p
JOIN pc ON p.model = pc.model
WHERE p.maker = 'B'

UNION

SELECT p.model, l.price
FROM product p
JOIN laptop l ON p.model = l.model
WHERE p.maker = 'B'

UNION

select p.model, pr.price
from product p
join printer pr on pr.model = p.model
where p.maker = 'B'

-- 8. Найти производителя, выпускающего ПК, но не ноутбуки.
SELECT DISTINCT maker from product pr
JOIN pc on pr.model = pc.model
AND maker NOT IN
(SELECT maker from product p where p.type = 'Laptop');

-- 9. Найти производителей ПК с процессором не менее 450 Мгц. Вывести поля: maker.
SELECT DISTINCT maker from product pr
JOIN pc on pr.model = pc.model 
WHERE pc.speed >= 450;

-- 10. Найти принтеры, имеющие самую высокую цену. Вывести поля: model, price.
SELECT model, price FROM printer
ORDER BY (price) DESC
LIMIT 2

-- 11. Найти среднюю скорость ПК.
SELECT AVG(speed) FROM PC;

-- 12. Найти среднюю скорость ноутбуков, цена которых превышает 1000 долларов.
SELECT AVG(speed) FROM laptop WHERE price > CAST(1000 AS MONEY);

-- 13. Найти среднюю скорость ПК, выпущенных производителем A.
SELECT AVG(speed) FROM PC
JOIN product pr ON pc.model = pr.model
WHERE pr.maker = 'A';

-- 14. Для каждого значения скорости процессора найти среднюю стоимость ПК с такой же скоростью. Вывести поля: скорость, средняя цена.
SELECT speed, AVG(price::numeric) FROM pc
GROUP BY speed
ORDER BY (speed);

-- 15. Найти размеры жестких дисков, совпадающих у двух и более PC. Вывести поля: hd.
SELECT hd from pc
GROUP BY (hd)
HAVING COUNT(pc.model) >= 2
ORDER BY(hd);

-- 16. Найти пары моделей PC, имеющих одинаковые скорость процессора и RAM. 
-- В результате каждая пара указывается только один раз, т.е. (i,j), но не (j,i). 
-- Порядок вывода полей: модель с большим номером, модель с меньшим номером, скорость, RAM.
SELECT p1.model, p2.model, p1.speed, p1.ram FROM product pr
JOIN pc p1 ON pr.model = p1.model
JOIN pc p2 ON pr.model = p2.model 
AND p1.speed = p2.speed AND p1.ram = p2.ram AND p1.code > p2.code;

-- 17. Найти модели ноутбуков, скорость которых меньше скорости любого из ПК. Вывести поля: type, model, speed.
SELECT pr.type, pr.model, l.speed FROM product pr
JOIN laptop l ON pr.model = l.model
WHERE l.speed < (
    SELECT MIN(speed)
    FROM pc
);

-- 18. Найти производителей самых дешевых цветных принтеров. Вывести поля: maker, price.
SELECT pr.maker, price FROM printer p
JOIN product pr ON p.model = pr.model
WHERE p.color = 'y'
ORDER BY(price)
LIMIT 3;

-- 19. Для каждого производителя найти средний размер экрана выпускаемых им ноутбуков. Вывести поля: maker, средний размер экрана.
SELECT pr.maker, AVG(l.screen) FROM product pr
JOIN laptop l ON pr.model = l.model
GROUP BY(pr.maker) 
ORDER BY(maker);

-- 20. Найти производителей, выпускающих по меньшей мере три различных модели ПК. Вывести поля: maker, число моделей.
SELECT pr.maker, COUNT(DISTINCT pr.model) FROM product pr
JOIN pc ON pr.model = pc.model
GROUP BY(pr.maker) 
HAVING COUNT(DISTINCT pr.model) >= 3;

-- 21. Найти максимальную цену ПК, выпускаемых каждым производителем. Вывести поля: maker, максимальная цена.
SELECT pr.maker, MAX(pc.price) FROM product pr
JOIN pc ON pr.model = pc.model
GROUP BY(pr.maker);

-- 22. Для каждого значения скорости процессора ПК, превышающего 600 МГц, найти среднюю цену ПК с такой же скоростью. Вывести поля: speed, средняя цена.
SELECT p.speed, AVG(p.price::numeric) FROM product pr
JOIN pc p ON pr.model = p.model
WHERE p.speed >= 600
GROUP BY p.speed

-- 23. Найти производителей, которые производили бы как ПК, так и ноутбуки со скоростью не менее 750 МГц. Вывести поля: maker
SELECT DISTINCT maker
FROM product
WHERE model IN (SELECT model FROM pc)
  AND model IN (SELECT model FROM laptop)

-- 24. Перечислить номера моделей любых типов, имеющих самую высокую цену по всей имеющейся в базе данных продукции.
SELECT model, price FROM pc WHERE price = (SELECT MAX(price) FROM pc)
UNION
SELECT model, price FROM laptop WHERE price = (SELECT MAX(price) FROM laptop)
UNION
SELECT model, price FROM printer WHERE price = (SELECT MAX(price) FROM printer)
ORDER BY (price);

-- 25. Найти производителей принтеров, которые производят ПК с наименьшим объемом RAM и с самым быстрым процессором среди всех ПК,
-- имеющих наименьший объем RAM. Вывести поля: maker
select distinct pr1.maker from product pr1
join pc p on pr1.model = p.model
where p.ram = (
    select min(ram) from pc
)
and p.speed = (
    select max(speed) from pc
    where ram = (select min(ram) from pc)
)
and pr1.maker in (
    select maker from product
    where type = 'printer'
);