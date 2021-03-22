insert into Users (username, password, enabled) values ('Simi', '{noop}Siminitas', true);
insert into Users (username, password, enabled) values ('Patri', '{noop}Patricitas', true);

insert into Authorities (username, authority) values ('Simi', 'ROLE_USER');
insert into Authorities (username, authority) values ('Patri', 'ROLE_USER');

insert into Groups (group_name) values ('Family');

insert into Group_Members (username, group_id) values ('Simi', 1);
insert into Group_Members (username, group_id) values ('Patri', 1);

insert into Group_Authorities (authority, group_id) values ('ROLE_USER', 1);