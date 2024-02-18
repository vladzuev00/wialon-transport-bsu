UPDATE trackers_last_data SET data_id = NULL;
UPDATE tracker_mileages SET urban = 0, country = 0;
DELETE FROM data;
DELETE FROM cities;
DELETE FROM searching_cities_processes;
DELETE FROM addresses;
ALTER SEQUENCE data_id_seq RESTART WITH 1;
ALTER SEQUENCE parameters_id_seq RESTART WITH 1;