INSERT INTO users(id, email, encrypted_password, role)
VALUES(255, 'vladzuev.00@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'USER'),
      (256, 'youtube-vladzuev-00@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'USER'),
      (257, 'ivan-ivanov@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'ADMIN');

INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(255, '11112222333344445555', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336934', 255),
      (256, '11112222333344445556', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336935', 255),
      (257, '00000000000000003009', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336936', 255);

INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry)
VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'first-city', 'first-country', ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326)),
      (257, ST_GeomFromText('POLYGON((3 3, 3 4, 4 4, 4 3, 3 3))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'second-city', 'second-country', ST_GeomFromText('POLYGON((3 3, 4 3, 4 4, 3 3))', 4326)),
      (259, ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326), ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'third-city', 'third-country', ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326));

INSERT INTO cities(id, address_id) VALUES(258, 257), (260, 259);

INSERT INTO locations(id, date_time, latitude, longitude, speed, course, altitude, satellite_count, hdop, inputs, outputs, analog_inputs, driver_key_code, tracker_id, address_id)
VALUES (252, '2019-10-21 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257),
       (253, '2019-10-22 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257),
       (254, '2019-10-23 14:39:50', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257),
       (255, '2019-10-24 14:39:51', 53.232, 27.3433, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257),
       (256, '2019-10-24 14:39:52', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257),
       (257, '2019-10-26 14:39:53', 53.233, 27.3434, 8, 9, 10, 11, 12.4, 13, 14, ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 257);

INSERT INTO parameters(id, name, type, value, location_id) VALUES(257, 'name', 'INTEGER', '44', 256);