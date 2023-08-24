DELETE
FROM TYPE_POST_ITEM;
DELETE
FROM STATUS_DELIVERY;
DELETE
FROM POST_OFFICES;
DELETE
FROM ADDRESSES;





--DROP TABLE IF EXISTS POST_OFFICES CASCADE;
--DROP TABLE IF EXISTS ADDRESSES CASCADE;
--DROP TABLE IF EXISTS TYPE_POST_ITEM CASCADE;
--DROP TABLE IF EXISTS STATUS_DELIVERY CASCADE;
--DROP TABLE IF EXISTS DELIVERY_STAGE CASCADE;
--DROP TABLE IF EXISTS POST_ITEMS CASCADE;







--INSERT INTO TYPE_POST_ITEM (ID, TYPE_POST_ITEM, ALIAS)
--VALUES (1, 'Письмо', 'letter'),
--       (2, 'Посылка', 'package'),
--       (3, 'Бандероль', 'parcel'),
--       (4, 'Открытка', 'postcard');
----
--INSERT INTO STATUS_DELIVERY (ID, STATUS)
--VALUES (1, 'Зарегистрировано в отделении'),
--       (2, 'Прибыло в отделение'),
--       (3, 'Отправлено из отделения'),
--       (4, 'Готово к выдаче'),
--       (5, 'Отправлено');
--
--INSERT INTO POST_OFFICES(IND, NAME)
--VALUES (123456, 'Отделение №1'),
--       (654321, 'Отделение №2'),
--       (987654, 'Отделение №3');
--
--INSERT INTO ADDRESSES (ID, IND, CITY, STREET, HOUSE_NUMBER, FLAT_NUMBER)
--VALUES (1, 123456, 'C1', 'Огуречная', 12, 3),
--       (2, 123456, 'C1', 'Помидорная', 10, 5),
--       (3, 123456, 'C1', 'Картофельная', 8, 7),
--       (4, 654321, 'C2', 'Грушевая', 12, 3),
--       (5, 654321, 'C2', 'Абрикосовая', 10, 5),
--       (6, 654321, 'C2', 'Ананасовая', 8, 7),
--       (7, 987654, 'C3', 'Белая', 12, 3),
--       (8, 987654, 'C3', 'Красная', 10, 5),
--       (9, 987654, 'C3', 'Желтая', 8, 7);