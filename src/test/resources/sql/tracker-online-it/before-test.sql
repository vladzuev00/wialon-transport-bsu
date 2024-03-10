INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry)
VALUES(258, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326),
       ST_SetSRID(ST_Point(53.050286, 24.873635), 4326), 'city', 'country',
       ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326));

INSERT INTO data(id, date_time, latitude, longitude, speed, course, altitude, amount_of_satellites, hdop,
                 inputs, outputs, analog_inputs, driver_key_code, tracker_id, address_id)
VALUES (252, CURRENT_TIMESTAMP - INTERVAL '7 MINUTE', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (253, CURRENT_TIMESTAMP - INTERVAL '2 MINUTE', 53.234, 27.3435, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258),
       (254, CURRENT_TIMESTAMP - INTERVAL '7 MINUTE', 53.235, 27.3436, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 256, 258),
       (255, CURRENT_TIMESTAMP - INTERVAL '6 MINUTE', 53.236, 27.3437, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 256, 258);