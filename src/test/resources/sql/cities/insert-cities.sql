INSERT INTO searching_cities_processes(id, bounds, search_step, total_points, handled_points, status)
VALUES(254, ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), 1, 36, 36, 'SUCCESS');

INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry)
VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'first-city', 'first-country', ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326)),
      (257, ST_GeomFromText('POLYGON((3 3, 3 4, 4 4, 4 3, 3 3))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'second-city', 'second-country', ST_GeomFromText('POLYGON((3 3, 4 3, 4 4, 3 3))', 4326)),
      (259, ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'third-city', 'third-country', ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326));

INSERT INTO cities(id, address_id, searching_cities_process_id)
VALUES(258, 257, 254),
      (260, 259, 254);