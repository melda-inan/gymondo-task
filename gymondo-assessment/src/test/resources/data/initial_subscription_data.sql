INSERT INTO public.customer_subscription
(customer_id, subscription_id, voucher_id, price, tax, start_date, end_date, status, trial)
VALUES( 1, 1, 1, 48.00, 4.80, '2021-09-12', '2021-12-12', 'A', false);

INSERT INTO public.customer_subscription
(customer_id, subscription_id, voucher_id, price, tax, start_date, end_date, status, trial, pause_date)
VALUES(1, 2, NULL, 90.00, 9.00, '2021-09-12', '2022-03-12', 'P', false, '2021-10-12');