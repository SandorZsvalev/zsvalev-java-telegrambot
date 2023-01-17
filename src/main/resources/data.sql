CREATE TABLE channel (
    id serial primary key,
    channel_name text,
    channel_tlg_id bigint
);

CREATE TABLE user_tbl (
    id serial primary key,
    user_name text
);

CREATE TABLE user_channel (
    id serial primary key,
    user_id int,
	channel_id int
);

CREATE TABLE user_schedule (
    id serial primary key,
    user_id int
);

CREATE TABLE telegram_channel_post (
    id serial primary key,
    channel_tlg_id bigint,
    message_id int,
    text_message text,
    date_message int
);

--CREATE TABLE if not exists users (
--id int auto_increment primary key,
--    name varchar(255),
--);

--CREATE TABLE if not exists bike (
--id int auto_increment primary key,
--    name varchar(255),
--    years_production int
--);
--
--CREATE TABLE if not exists parking (
--id int auto_increment primary key
--);


--INSERT INTO car (color, name, production_year) VALUES ('green','BMW', 1999);
--INSERT INTO car (color, name, production_year) VALUES ('red','Audi', 1980);
--INSERT INTO car (color, name, production_year) VALUES ('yellow','Opel', 1994);
--INSERT INTO car VALUES (1, 'blue', 'Mercedes', 2000);

--INSERT INTO bike (id, name, years_production) VALUES (1,'Honda', 2000);
--INSERT INTO bike (id, name, years_production) VALUES (2,'Yamaha', 2010);
--INSERT INTO bike (id, name, years_production) VALUES (3,'Suzuki', 2017);
