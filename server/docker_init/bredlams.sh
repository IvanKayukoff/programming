#!/usr/bin/env sh

psql -U sky -d postgres << END_OF_SQL
create table bredlam(creation timestamptz not null,endOfLight boolean not null,flagColor text not null,id serial primary key,name text not null,x integer not null,y integer not null);
create table human(id serial primary key,money integer not null,name text not null,bredlam_id integer references bredlam on delete cascade on update cascade);

insert into bredlam (creation, endOfLight, flagColor, name, x, y) values ('2018-10-08 12:18:02.833+03'::timestamp with time zone, 'TRUE', 'Yellow', 'P3402', 2, 13) returning id;

insert into bredlam (creation, endOfLight, flagColor, name, x, y) values ('2018-10-08 12:20:44.253+03'::timestamp with time zone, 'FALSE', 'Orange', 'Precious', 10, 10) returning id;

insert into human (money, name, bredlam_id) values (666000, 'Bleizard', 2) returning id;
insert into human (money, name, bredlam_id) values (148800, 'IvanUskov', 2) returning id;

insert into bredlam (creation, endOfLight, flagColor, name, x, y) values ('2018-10-08 12:15:06.608+03'::timestamp with time zone, 'FALSE', 'Red', 'Gorenje', -4, 3) returning id;

insert into human (money, name, bredlam_id) values (15000, 'Neki4', 3) returning id;
insert into human (money, name, bredlam_id) values (4000, 'Naushniki', 3) returning id;
insert into human (money, name, bredlam_id) values (5323, 'Sone4ka', 3) returning id;

insert into bredlam (creation, endOfLight, flagColor, name, x, y) values ('2018-10-08 12:11:07.093+03'::timestamp with time zone, 'TRUE', 'Blue', 'P3202', 5, 8) returning id;

insert into human (money, name, bredlam_id) values (10000, 'Cockoff', 4) returning id;
insert into human (money, name, bredlam_id) values (10000, 'Timlathy', 4) returning id;
insert into human (money, name, bredlam_id) values (50000, 'Dalik', 4) returning id;
insert into human (money, name, bredlam_id) values (20000, 'Kurman', 4) returning id;
insert into human (money, name, bredlam_id) values (15000, 'Rogolab', 4) returning id;
insert into human (money, name, bredlam_id) values (7000, 'Vaness', 4) returning id;
insert into human (money, name, bredlam_id) values (4000, 'Marygold', 4) returning id;
END_OF_SQL
