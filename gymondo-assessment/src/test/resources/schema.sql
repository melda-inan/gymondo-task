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