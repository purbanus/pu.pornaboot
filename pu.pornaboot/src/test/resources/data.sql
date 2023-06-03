delete from directory;
delete from property;
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 1, 'Dir A', '2023-05-14 02:03:04', null );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 2, 'Dir A/Dir B', '2023-05-15 02:03:04', 1 );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 3, 'Dir A/Dir B/Dir C', '2023-05-16 02:03:04', 2 );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 4, 'Dir A/Dir B/Dir D', '2023-05-17 02:03:04', 2 );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 5, 'Dir A/Dir E', '2023-05-18 02:03:04', 1 );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 6, 'Dir A/Dir E/Dir F', '2023-05-19 02:03:04', 5 );
insert into directory ( id, name, date_time_last_modified, parent_id ) values( 7, 'Dir A/Dir E/Dir G', '2023-05-20 02:03:04', 5 );

insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 10, 'Dir A File A', '2023-05-14 12:05:54', 123456, 'TOP', 'pipo review',1 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 11, 'Dir A File B', '2023-05-15 12:05:54', 123, 'TOP', null, 1 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 12, 'Dir A File C', '2023-05-16 12:05:54', 456, 'TOP', null, 1 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 13, 'Dir B File A', '2023-05-14 12:05:54', 123456, 'TOP', null, 2 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 14, 'Dir B File B', '2023-05-15 12:05:54', 123, 'TOP', null, 2 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 15, 'Dir B File C', '2023-05-16 12:05:54', 456, 'TOP', null, 2 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 16, 'Dir C File A', '2023-05-14 12:05:54', 123456, 'GOED', null, 3 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 17, 'Dir C File B', '2023-05-15 12:05:54', 123, 'GOED', null, 3 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 18, 'Dir C File C', '2023-05-16 12:05:54', 456, 'GOED', null, 3 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 19, 'Dir D File A', '2023-05-14 12:05:54', 123456, null, 'pipo review 2', 4 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 20, 'Dir D File B', '2023-05-15 12:05:54', 123, null, 'pipo review 2', 4 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 21, 'Dir D File C', '2023-05-16 12:05:54', 456, null, 'pipo review 2', 4 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 22, 'Dir E File A', '2023-05-14 12:05:54', 123456, 'MWAH', null, 5 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 23, 'Dir E File B', '2023-05-15 12:05:54', 123, 'MWAH', null, 5 );
insert into file ( id, name, date_time_last_modified, size, kwaliteit, review, directory_id ) values( 24, 'Dir E File C', '2023-05-16 12:05:54', 456, 'MWAH', null, 5 );

insert into property (id, name) values( 30, 'anal' );
insert into property (id, name) values( 31, 'asian' );
insert into property (id, name) values( 32, 'bdsm' );

insert into file_property_map (file_id, property_id) values( 10, 30 );
insert into file_property_map (file_id, property_id) values( 10, 31 );
insert into file_property_map (file_id, property_id) values( 10, 32 );
insert into file_property_map (file_id, property_id) values( 11, 30 );
insert into file_property_map (file_id, property_id) values( 11, 31 );
insert into file_property_map (file_id, property_id) values( 11, 32 );
insert into file_property_map (file_id, property_id) values( 12, 30 );
insert into file_property_map (file_id, property_id) values( 13, 30 );
insert into file_property_map (file_id, property_id) values( 14, 30 );
insert into file_property_map (file_id, property_id) values( 15, 31 );
insert into file_property_map (file_id, property_id) values( 16, 31 );
insert into file_property_map (file_id, property_id) values( 17, 31 );
insert into file_property_map (file_id, property_id) values( 18, 32 );
insert into file_property_map (file_id, property_id) values( 19, 32 );
insert into file_property_map (file_id, property_id) values( 20, 32 );
insert into file_property_map (file_id, property_id) values( 21, 30 );
insert into file_property_map (file_id, property_id) values( 22, 30 );
insert into file_property_map (file_id, property_id) values( 23, 30 );
insert into file_property_map (file_id, property_id) values( 24, 31 );

alter sequence hibernate_sequence restart with 500