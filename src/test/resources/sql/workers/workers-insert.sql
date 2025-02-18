insert into SUPPLIER (id, name, contact) values (10, 'fornecedorx', 'email@live.com');

insert into SUPPLIER (id, name, contact) values (11, 'fornecedory', 'email@live.com');

insert into RAW_MATERIAL(id, name, description, amount, cost, id_supplier) values (6, 'bastidor', 'bastidor mdf', 10, 10, 10);

insert into PROFIT_MARGIN (id, description, profit_value) values (10, 'margem 5%', 5.0);
insert into PROFIT_MARGIN (id, description, profit_value) values (20, 'margem 10%', 10.0);
insert into PROFIT_MARGIN (id, description, profit_value) values (30, 'margem 15%', 15.0);

insert into LABOR_TYPES (id, cost, labor_type) values (10, 10, 'bordado');

insert into WORKER (id, status, id_labor_type, id_profit_margin) values (4, 'EM_ANDAMENTO', 10, 20);
insert into WORKER_RAW_MATERIAL (worker_id, raw_material_id) values (4, 6);
insert into WORKER_SPLIT_TIME (worker_id, end_time, initial_time, total_time) values(4, '2025-01-20 21:48:41.000000', '2025-01-20 11:48:41.000000', 36000);