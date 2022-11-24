-- liquibase formatted sql

------------------------------------------------------------------------------------------------------------------
-- Пересоздание БД "phonebook"
-- Из flyWayDB пересоздание БД не работает, требуется запускать вручную из консоли SQL Shell
-- 
-- DROP DATABASE IF EXISTS phonebook;
-- CREATE DATABASE phonebook WITH OWNER = postgres  ENCODING = 'UTF8' LC_COLLATE = 'Russian_Russia.1251' LC_CTYPE = 'Russian_Russia.1251' TABLESPACE = pg_default CONNECTION LIMIT = -1 IS_TEMPLATE = False;
------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------
-- Создание таблицы телефонного справочника
------------------------------------------------------------------------------------------------------------------
-- changeset User:1667218636789-1
DROP TABLE IF EXISTS "employee";

-- changeset User:1667218636789-2
CREATE TABLE "employee"
(
    "id"           INTEGER PRIMARY KEY NOT NULL,
    "first_name"   VARCHAR(10)        NOT NULL,
    "last_name"    VARCHAR(20)        NOT NULL,
    "birthdate"    DATE               NOT NULL,
    "mobile_phone" CHAR(13)           NOT NULL,
    "work_phone"   VARCHAR(15),
    "email"        VARCHAR(41),
    "created_at"   TIMESTAMP DEFAULT now(),
    "job_id"       INTEGER
);

-- changeset User:1667218636789-3
DROP TABLE IF EXISTS "job";

-- changeset User:1667218636789-4
CREATE TABLE "job"
(
    "id"        INTEGER PRIMARY KEY NOT NULL,
    "company"   VARCHAR(200)       NOT NULL,
    "job_title" VARCHAR(150)       NOT NULL,
    "address"   VARCHAR(255)
);

-- changeset User:1667218636789-5
ALTER TABLE "employee"
    ADD CONSTRAINT "employee_job_foreign_key" FOREIGN KEY ("job_id") REFERENCES "job" ("id");

------------------------------------------------------------------------------------------------------------------
-- создание последовательности для заполнения id employee
-- ! для БД postgresSql необходима последовательность для генерации id
------------------------------------------------------------------------------------------------------------------
-- changeset User:1667218636789-6
DROP SEQUENCE IF EXISTS public.employee_id_seq;

-- changeset User:1667218636789-7
CREATE SEQUENCE IF NOT EXISTS public.employee_id_seq
    INCREMENT 1
    START 100
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- changeset User:1667218636789-8
ALTER SEQUENCE public.employee_id_seq
    OWNER TO postgres;

------------------------------------------------------------------------------------------------------------------
-- создание последовательности для заполнения id job
-- ! для БД postgresSql необходима последовательность для генерации id
------------------------------------------------------------------------------------------------------------------
-- changeset User:1667218636789-9
DROP SEQUENCE IF EXISTS public.job_id_seq;

-- changeset User:1667218636789-10
CREATE SEQUENCE IF NOT EXISTS public.job_id_seq
    INCREMENT 1
    START 100
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- changeset User:1667218636789-11
ALTER SEQUENCE public.job_id_seq
    OWNER TO postgres;

------------------------------------------------------------------------------------------------------------------
-- Наполнение таблицы Справочник "Место работы" данными
------------------------------------------------------------------------------------------------------------------
-- changeset User:1667218636789-12
INSERT INTO job(id, company, job_title, address)
VALUES
       ( 1,'SpaceX', 'Инженер SpaceX','Address SpaceX'),
       ( 2,'Amazon', 'Глава Amazon','AddressAmazon'),
       ( 3,'LVMH','Председатель LVMH','AddressLVMH'),
       ( 4,'Microsoft','Владелец Microsoft','AddressMicrosoft'),
       ( 5,'Oracle','Архитектор Oracle','AddressOracle'),
       ( 6,'Google','Специалист Google','AddressGoogle'),
       ( 7,'Meta','Директор Meta','AddressMeta'),
       ( 8,'Google','Director Google','AddressGoogle'),
       ( 9,'Berkshire Hathaway','Основатель Berkshire Hathaway','AddressBerkshire Hathaway'),
       (10,'Microsoft','Owner Microsoft','AddressMicrosoft'),
       (11,'L-Oreal','Наследница L-Oreal','Address L-Oreal'),
       (12,'Reliance Industries','CEO Reliance Industries','AddressReliance Industries'),
       (13,'Grupo Carso','CEO Grupo Carso','AddressGrupo Carso'),
       (14,'Adani Group','CEO Adani Group','AddressAdani Group'),
       (15,'Nongfu Spring','CEO Nongfu Spring','AddressNongfu Spring'),
       (16,'Inditex','CEO Inditex','AddressInditex'),
       (17,'Bloomberg','CEO Bloomberg','AddressBloomberg'),
       (18,'Walmart','Owner1','AddressWalmart'),
       (19,'Walmart','Owner2','AddressWalmart'),
       (20,'Walmart','Owner3','AddressWalmart'),
       (21,'CBR.RU','Главный инженер CBR.RU','Address CBR.RU'),
       (31,'SpaceX', 'Программист','Address SpaceX'),
       (32,'Amazon', 'Программист','AddressAmazon'),
       (33,'LVMH','Программист','AddressLVMH'),
       (34,'Microsoft','Программист','AddressMicrosoft'),
       (35,'Oracle','Программист','AddressOracle'),
       (36,'Google','Программист','AddressGoogle'),
       (37,'Meta','Программист','AddressMeta'),
       (38,'Google','Программист','AddressGoogle'),
       (39,'Berkshire Hathaway','Программист','AddressBerkshire Hathaway'),
       (40,'Microsoft','Программист','AddressMicrosoft'),
       (41,'L-Oreal','Программист','Address L-Oreal'),
       (42,'Reliance Industries','Программист','AddressReliance Industries'),
       (43,'Grupo Carso','Программист','AddressGrupo Carso'),
       (44,'Adani Group','Программист','AddressAdani Group'),
       (45,'Nongfu Spring','Программист','AddressNongfu Spring'),
       (46,'Inditex','Программист','AddressInditex'),
       (47,'Bloomberg','Программист','AddressBloomberg'),
       (48,'Walmart','Программист','AddressWalmart');
       (50,'Google','CEO', 'USA 117153, Google');
       (51,'Microsoft', 'Senior developer', 'Moscow-City')

------------------------------------------------------------------------------------------------------------------
-- Наполнение таблицы "Телефонный справочник" данными
------------------------------------------------------------------------------------------------------------------
-- changeset User:1667218636789-13
INSERT INTO employee(id,first_name,last_name, work_phone, mobile_phone, email, birthdate, job_id)
VALUES
       (1,'Илон','Маск', '495-111-11-01','123-111-11-01','ilon@cbr.ru',   '1971-06-28', 1),
       (2,'Джефф','Безос','495-111-11-02','123-111-11-02','n2@cbr.ru',    '1964-01-12', 2),
       (3,'Бернар','Арно','495-111-11-03','123-111-11-03','n3@cbr.ru',    '1949-03-05', 3),
       (4,'Билл','Гейтс','495-111-11-04','123-111-11-04','n4@cbr.ru',     '1955-10-28', 4),
       (5,'Ларри','Эллисон','495-111-11-05','123-111-11-05','n5@cbr.ru',  '1944-08-17', 5),
       (6,'Ларри','Пейдж','495-111-11-06','123-111-11-06','n6@cbr.ru',    '1973-03-26', 6),
       (7,'Марк','Цукерберг','495-111-11-07','123-111-11-07','n7@cbr.ru', '1984-05-14', 7),
       (8,'Сергей','Брин','495-111-11-08','123-111-11-08','n8@cbr.ru',    '1973-08-21', 8),
       (9,'Уоррен','Баффетт','495-111-11-09','123-111-11-09','n9@cbr.ru', '1930-08-30', 9),
       (10,'Стив','Балмер','495-111-11-10','123-111-11-10','n10@cbr.ru',  '1956-03-24',10),
       (11,'Франси','Беттанкур','495-111-11-11','123-111-11-11','n11@cbr.ru', '1953-07-10',11),
       (12,'Мукеш','Амбани','495-111-11-12','123-111-11-12','n12@cbr.ru',   '1957-04-19',12),
       (13,'Карлос','Слим','495-111-11-13','123-111-11-13','n13@cbr.ru',    '1940-01-28',13),
       (14,'Гаутам','Адани','495-111-11-14','123-111-11-14','n14@cbr.ru',   '1000-01-01',14),
       (15,'Чжун','Шаньшань','495-111-11-15','123-111-11-15','n15@cbr.ru',  '1954-12-01',15),
       (16,'Амансио','Ортега','495-111-11-16','123-111-11-16','n16@cbr.ru', '1936-03-28',16),
       (17,'Майкл','Блумберг','495-111-11-17','123-111-11-17','n17@cbr.ru', '1942-02-14',17),
       (18,'Джим','Уолтон','495-111-11-18','123-111-11-18','n18@cbr.ru',    '1980-01-01',18),
       (19,'Элис','Уолтон','495-111-11-19','123-111-11-19','n19@cbr.ru',    '1949-10-02',19),
       (20,'Роб','Уолтон','495-111-11-20','123-111-11-20','n20@cbr.ru',     '1949-10-02',20),
       (21,'Иван','Маск', '495-111-11-21','123-111-11-21','n21@cbr.ru',   '1981-06-28', 31),
       (22,'Иван','Безос','495-111-11-22','123-111-11-22','n22@cbr.ru',    '1984-01-12', 32),
       (23,'Иван','Арно','495-111-11-23','123-111-11-23','n23@cbr.ru',    '1989-03-05', 33),
       (24,'Иван','Гейтс','495-111-11-24','123-111-11-24','n24@cbr.ru',     '1985-10-28', 34),
       (25,'Иван','Эллисон','495-111-11-25','123-111-11-25','n25@cbr.ru',  '1984-08-17', 35),
       (26,'Иван','Пейдж','495-111-11-26','123-111-11-26','n26@cbr.ru',    '1983-03-26', 36),
       (27,'Иван','Цукерберг','495-111-11-27','123-111-11-27','n27@cbr.ru', '1994-05-14', 37),
       (28,'Иван','Брин','495-111-11-28','123-111-11-28','n28@cbr.ru',    '1983-08-21', 38),
       (29,'Иван','Баффетт','495-111-11-29','123-111-11-29','n29@cbr.ru', '1980-08-30', 39),
       (30,'Иван','Балмер','495-111-11-30','123-111-11-30','n30@cbr.ru',  '1986-03-24',40),
       (31,'Анна','Беттанкур','495-111-11-31','123-111-11-31','n31@cbr.ru', '1983-07-10',41),
       (32,'Иван','Амбани','495-111-11-32','123-111-11-32','n32@cbr.ru',   '1987-04-19',42),
       (33,'Иван','Слим','495-111-11-33','123-111-11-33','n33@cbr.ru',    '1980-01-28',43),
       (34,'Иван','Адани','495-111-11-34','123-111-11-34','n34@cbr.ru',   '1080-01-01',44),
       (35,'Иван','Шаньшань','495-111-11-35','123-111-11-35','n35@cbr.ru',  '1984-12-01',45),
       (36,'Иван','Ортега','495-111-11-36','123-111-11-36','n36@cbr.ru', '1986-03-28',46),
       (37,'Иван','Блумберг','495-111-11-37','123-111-11-37','n37@cbr.ru', '1982-02-14',47),
       (38,'Андрей','Уолтон','495-111-11-38','123-111-11-38','n38@cbr.ru',    '1980-01-01',48),
       (39,'Борис','Уолтон','495-111-11-39','123-111-11-39','n39@cbr.ru',    '1989-10-02',48),
       (40,'Владимир','Уолтон','495-111-11-40','123-111-11-40','n40@cbr.ru',     '1989-10-02',48),
       (41,'Igor','Belan','495-236-33-44','123-444-44-44','igor@cbr.ru',    '2022-11-15',21),
       (50,'Ilya','Pimenov', '495-263-73-73', '123-555-55-55','ipimenov@gmail.com', '1982-12-30', 50);

-- changeset User:1667218636789-14
INSERT INTO employee(id,first_name,last_name, work_phone, mobile_phone, email, birthdate)
VALUES
       (51,'Иван','Иванов','123-456-78-90','123-456-78-91', 'ivan@gmail.com', '1985-09-23');
