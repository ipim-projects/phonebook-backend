-- liquibase formatted sql

-- changeset User:1667218636789-1
CREATE TABLE "employee"
(
    "id"           SERIAL PRIMARY KEY NOT NULL,
    "first_name"   VARCHAR(50)        NOT NULL,
    "last_name"    VARCHAR(50)        NOT NULL,
    "birthdate"    DATE               NOT NULL,
    "mobile_phone" VARCHAR(15)        NOT NULL,
    "work_phone"   VARCHAR(15),
    "email"        VARCHAR(100),
    "created_at"   TIMESTAMP DEFAULT now(),
    "job_id"       INTEGER
);

-- changeset User:1667218636789-2
CREATE TABLE "job"
(
    "id"        SERIAL PRIMARY KEY NOT NULL,
    "company"   VARCHAR(200)       NOT NULL,
    "job_title" VARCHAR(150)       NOT NULL,
    "address"   VARCHAR(255)
);

-- changeset User:1667218636789-3
ALTER TABLE "employee"
    ADD CONSTRAINT "employee_job_foreign_key" FOREIGN KEY ("job_id") REFERENCES "job" ("id");