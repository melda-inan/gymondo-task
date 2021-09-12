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



