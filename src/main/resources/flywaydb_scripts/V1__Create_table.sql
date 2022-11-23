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
DROP TABLE IF EXISTS public.employee;

CREATE TABLE public.employee (id INTEGER PRIMARY KEY NOT NULL, first_name VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL, birthdate date NOT NULL, mobile_phone CHAR(13) NOT NULL, work_phone CHAR(13), email VARCHAR(100), job_id INTEGER);

ALTER TABLE IF EXISTS public.employee OWNER to postgres;

ALTER TABLE IF EXISTS employee ADD CONSTRAINT employee_email_key UNIQUE (email);

ALTER TABLE IF EXISTS employee ADD CONSTRAINT employee_mobile_phone_key UNIQUE (mobile_phone);

------------------------------------------------------------------------------------------------------------------
-- Создание таблицы место работы
------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.job;

CREATE TABLE public.job (id INTEGER PRIMARY KEY NOT NULL, company VARCHAR(200) NOT NULL, job_title VARCHAR(150) NOT NULL, address VARCHAR(255));

ALTER TABLE IF EXISTS public.job OWNER to postgres;

ALTER TABLE IF EXISTS employee ADD CONSTRAINT employee_job_foreign_key FOREIGN KEY (job_id) REFERENCES job (id);


------------------------------------------------------------------------------------------------------------------
-- создание последовательности для заполнения id employee
-- ! для БД postgresSql необходима последовательность для генерации id
------------------------------------------------------------------------------------------------------------------
DROP SEQUENCE IF EXISTS public.employee_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.employee_id_seq
    INCREMENT 1
    START 100
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.employee_id_seq
    OWNER TO postgres;

------------------------------------------------------------------------------------------------------------------
-- создание последовательности для заполнения id job
-- ! для БД postgresSql необходима последовательность для генерации id
------------------------------------------------------------------------------------------------------------------
DROP SEQUENCE IF EXISTS public.job_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.job_id_seq
    INCREMENT 1
    START 100
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.job_id_seq
    OWNER TO postgres;
