insert into warehouses(id, client, family, size, uuid ) values (12345, 'Cliente A', 'EST', 4, '0071128c-79c7-43fb-a65d-b26cee852f67');
insert into warehouses(id, client, family, size, uuid ) values (12346, 'Cliente A', 'ROB', 10, '56865f88-157b-487d-ad56-e89e65a98a00');
insert into warehouses(id, client,family, size, uuid ) values (12347, 'Cliente B', 'EST', 15, '850a1c1a-4f22-4c9f-90c0-f6b0e37d701a');
insert into racks(id, warehouse_id, uuid, type) values (1111, 12345, '5d2f9f2a-d546-4b4c-9452-b36fa6b4f1a5', 'A');
insert into racks(id, warehouse_id, uuid, type) values (1112, 12345, 'a92586a8-43fc-489f-8560-f8609cb4bff2', 'B');
insert into racks(id, warehouse_id, uuid, type) values (2222, 12347, '7caee8e0-2df2-4967-a551-f7b3f703b114', 'A');
insert into racks(id, warehouse_id, uuid, type) values (3333, 12347, 'ec1ecb66-f1e9-4a86-a17c-3ec4fbbcf7f9', 'A');

