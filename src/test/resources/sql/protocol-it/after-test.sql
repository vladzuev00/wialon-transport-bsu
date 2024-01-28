UPDATE trackers_last_data SET data_id = NULL;
DELETE FROM data;
DELETE FROM cities;
DELETE FROM searching_cities_processes;
DELETE FROM addresses;