INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry)
VALUES(258, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326),
       ST_SetSRID(ST_Point(53.050286, 24.873635), 4326), 'city', 'country',
       ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326));

INSERT INTO data(id, date_time, latitude, longitude, speed, course, altitude, amount_of_satellites, reduction_precision,
                 inputs, outputs, analog_inputs, driver_key_code, tracker_id, address_id)
VALUES (252, '2019-10-21 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (253, '2019-10-22 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (254, '2019-10-23 14:39:50', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (255, '2019-10-24 14:39:51', 53.232, 27.3433, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (256, '2019-10-24 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (257, '2019-10-26 14:39:53', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258);

INSERT INTO parameters(id, name, type, value, data_id) VALUES(257, 'name', 'INTEGER', '44', 256);