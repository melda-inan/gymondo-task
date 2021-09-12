CREATE TABLE customer (
	id SERIAL,
	first_name varchar(50),
	last_name varchar(50),
	email varchar(255) NOT NULL,
	password varchar(50),
	constraint customer_pk primary key (id)
);

CREATE TABLE product (
	id SERIAL,
	name varchar(100) NOT NULL,
	description varchar(500),
	constraint product_pk primary key (id)
);

CREATE TABLE subscription (
	id SERIAL,
	product_id Integer NOT NULL,
	month_duration Integer NOT NULL,
	price NUMERIC(8,2),
	tax_percentage Integer,
	constraint subscription_pk primary key (id),
	constraint subscription_product_fk foreign key (product_id) references product(id)
); 

CREATE TABLE voucher (
	id SERIAL,
	code varchar(50),
	discount_type Integer NOT NULL,
	discount_percentage Integer,
	discount_amount NUMERIC(8,2),
	constraint voucher_pk primary key (id)
); 

CREATE TABLE product_voucher (
	product_id Integer NOT NULL,
	voucher_id Integer NOT NULL,
	constraint product_voucher_product_fk foreign key (product_id) references product(id),
	constraint product_voucher_fk foreign key (voucher_id) references voucher(id)
); 

CREATE TABLE customer_subscription (
	id SERIAL,
	customer_id Integer NOT NULL,
	subscription_id Integer NOT NULL,
	voucher_id Integer,
	price NUMERIC(8,2),
	tax NUMERIC(8,2),
	start_date date,
	end_date date,
	status varchar(1),
	trial boolean,
	trial_start_date date,
	trial_end_date date,
	pause_date date,
	constraint customer_subscription_pk primary key (id),
	constraint customer_subscription_customer_fk foreign key (customer_id) references customer(id),
	constraint customer_subscription_fk foreign key (subscription_id) references subscription(id),
	constraint customer_subscription_voucher_fk foreign key (voucher_id) references voucher(id)
); 

INSERT INTO customer (first_name, last_name, email) values('Harry', 'Potter', 'thechosenone@gmail.com');
INSERT INTO customer (first_name, last_name, email) values('Hermione', 'Granger', 'h.granger@gmail.com');
INSERT INTO customer (first_name, last_name, email) values('Ron', 'Weasley', 'captainronweasley@gmail.com');

INSERT into product (name, description) values('Yoga Power', 'Unleash Your Power Within Through Athletic-Based Yoga');
INSERT into product (name, description) values('Salsa Fusion', 'Join Our High-Energy Dance Party to Burn Calories While Having Fun!');
INSERT into product (name, description) values('Fat Blaster', 'A Calorie-Crushing Program to Shed Unwanted Fat');

insert into subscription (product_id, month_duration, price, tax_percentage) values((select id from product where name = 'Yoga Power'), 3, 60, 10);
insert into subscription (product_id, month_duration, price, tax_percentage) values((select id from product where name = 'Salsa Fusion'), 6, 90, 10);
insert into subscription (product_id, month_duration, price, tax_percentage) values((select id from product where name = 'Fat Blaster'), 12, 120, 10);

insert INTO voucher (code, discount_type, discount_percentage) values('WELCOMENEWBY', 0, 20);
insert INTO voucher (code, discount_type, discount_amount) values('SUMMERFIT', 1, 30);

insert into product_voucher (product_id, voucher_id) values((select id from product where name = 'Yoga Power'), (select id from voucher where code = 'WELCOMENEWBY'));
insert into product_voucher (product_id, voucher_id) values((select id from product where name = 'Yoga Power'), (select id from voucher where code = 'SUMMERFIT'));
insert into product_voucher (product_id, voucher_id) values((select id from product where name = 'Fat Blaster'), (select id from voucher where code = 'SUMMERFIT'));
insert into product_voucher (product_id, voucher_id) values((select id from product where name = 'Salsa Fusion'), (select id from voucher where code = 'WELCOMENEWBY'));


