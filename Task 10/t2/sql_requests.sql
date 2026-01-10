--Найти номер модели, скорость и размер жесткого диска для всех ПК стоимостью менее 500 долларов.
select model,speed,hd from pc
where price < 500.0;

--Найти производителей принтеров. Вывести поля: maker.
select maker from product
where type = 'Printer';

--Найти номер модели, объем памяти и размеры экранов ноутбуков, цена которых превышает 1000 долларов.
select model, ram, screen from laptop
where price > 1000.0;

--Найти все записи таблицы Printer для цветных принтеров.
select * from printer
where color = 'y';

--Найти номер модели, скорость и размер жесткого диска для ПК, имеющих скорость cd 12x или 24x и цену менее 600 долларов.
select model, speed, hd from pc
where (cd = '12x' or cd = '24x') and price < 600.0;

--Указать производителя и скорость для тех ноутбуков, которые имеют жесткий диск объемом не менее 100 Гбайт.
select p.maker, l.speed from laptop as l
join product p on l.model = p.model
where l.hd >= 100;

--Найти номера моделей и цены всех продуктов (любого типа), выпущенных производителем B (латинская буква).
select p.model, pc.price from product p
join pc on p.model = pc.model
where p.maker = 'B'

union all

select p.model, l.price from product p
join laptop l on p.model = l.model
where p.maker = 'B'

union all

select p.model, pr.price from product p
join printer pr on p.model = pr.model
where p.maker = 'B'

order by model;

--Найти производителя, выпускающего ПК, но не ноутбуки.
select maker from product
where type = 'PC' and type != 'Laptop';

--Найти производителей ПК с процессором не менее 450 Мгц. Вывести поля: maker.
select p.maker from product p
join pc on pc.model = p.model
where pc.speed >= 450;

--Найти принтеры, имеющие самую высокую цену. Вывести поля: model, price.
select model, price from printer
where price = (SELECT max(price) from printer);

--Найти среднюю скорость ПК.
select avg(speed) as avg_speed from pc;

--Найти среднюю скорость ноутбуков, цена которых превышает 1000 долларов.
select avg(speed) as avg_speed from laptop
where price > 1000;

--Найти среднюю скорость ПК, выпущенных производителем A.
select avg(pc.speed) from pc
join product p on p.model = pc.model
where p.maker = 'A';

--Для каждого значения скорости процессора найти среднюю стоимость ПК с такой же скоростью. Вывести поля: скорость, средняя цена.
select speed, avg(price) from pc
group by speed 
order by speed;

--Найти размеры жестких дисков, совпадающих у двух и более PC. Вывести поля: hd.
select hd from pc
group by hd
having count(*) >= 2;

--Найти пары моделей PC, имеющих одинаковые скорость процессора и RAM. В результате каждая пара указывается только один раз, т.е. (i,j), но не (j,i), Порядок вывода полей: модель с большим номером, модель с меньшим номером, скорость, RAM.
select pc1.model as model_h, pc2.model as model_l, pc1.speed, pc1.ram
from pc pc1
join pc pc2 on pc1.speed = pc2.speed 
and pc1.ram = pc2.ram 
and pc1.model > pc2.model;

--Найти модели ноутбуков, скорость которых меньше скорости любого из ПК. Вывести поля: type, model, speed.

select p.type, l.model, l.speed
from laptop l
join product p on l.model = p.model
where l.speed < (select min(speed) from pc);

--Найти производителей самых дешевых цветных принтеров. Вывести поля: maker, price.
select p.maker, min(pr.price) from printer pr
join product p on p.model = pr.model
where pr.color = 'y'
group by p.maker;

--Для каждого производителя найти средний размер экрана выпускаемых им ноутбуков. Вывести поля: maker, средний размер экрана.
select p.maker, avg(l.screen) from laptop l
join product p on p.model = l.model
group by p.maker;

--Найти производителей, выпускающих по меньшей мере три различных модели ПК. Вывести поля: maker, число моделей.
select p.maker, count(*) as count_pcs from pc
join product p on p.model = pc.model
group by p.maker
having count(*) >= 3;

--Найти максимальную цену ПК, выпускаемых каждым производителем. Вывести поля: maker, максимальная цена.
select p.maker, max(pc.price) as max_price from pc
join product p on p.model = pc.model
group by p.maker
order by p.maker;

--Для каждого значения скорости процессора ПК, превышающего 600 МГц, найти среднюю цену ПК с такой же скоростью. Вывести поля: speed, средняя цена.
select speed, round(avg(price),2) as avg_price from pc
where speed > 600
group by speed
order by speed;

--Найти производителей, которые производили бы как ПК, так и ноутбуки со скоростью не менее 750 МГц. Вывести поля: maker
with pc_makers as (
	select distinct p.maker from pc
	join product p on pc.model = p.model
	where pc.speed >= 750
),
l_makers as (
	select distinct p.maker from laptop l
	join product p on l.model = p.model
	where l.speed >= 750
)
select pm.maker from pc_makers pm
join l_makers lm on pm.maker = lm.maker;

--Перечислить номера моделей любых типов, имеющих самую высокую цену по всей имеющейся в базе данных продукции.
with all_products as (
select model, price from pc
union all
select model, price from laptop
union all
select model, price from printer
)
select model
from all_products
where price = (select MAX(price) from all_products);

--Найти производителей принтеров, которые производят ПК с наименьшим объемом RAM и с самым быстрым процессором среди всех ПК, имеющих наименьший объем RAM. Вывести поля: maker
select distinct p.maker
from pc
join product p on pc.model = p.model
where 
    pc.ram = (select min(ram) from pc)
    and
    pc.speed = (
        select max(speed)
        from pc
        where ram = (select min(ram) from pc)
    )
    and
    exists (
        select 1
        from product pr
        where pr.maker = p.maker
          and pr.type = 'Printer'
    );