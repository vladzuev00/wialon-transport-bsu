INSERT INTO users(id, email, encrypted_password, role)
VALUES(255, 'vladzuev.00@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'USER');

--row should be inserted by trigger in tracker_odometers with 1 as id
INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(255, '11112222333344445555', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336934', 255);
