INSERT INTO searching_cities_processes(id, bounds, search_step, total_points, handled_points, status)
VALUES(254, ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), 1, 36, 36, 'SUCCESS');

INSERT INTO cities(id, address_id, searching_cities_process_id)
VALUES(258, 257, 254),
      (260, 259, 254);