INSERT INTO users(id, email, encrypted_password, role)
VALUES(255, 'vladzuev.00@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'USER');

INSERT INTO users(id, email, encrypted_password, role)
VALUES(256, 'youtube-vladzuev-00@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'USER');

INSERT INTO users(id, email, encrypted_password, role)
VALUES(257, 'ivan-ivanov@mail.ru', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', 'ADMIN');

INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(255, '11112222333344445555', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336934', 255);

INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(256, '11112222333344445556', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336935', 255);

INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(257, '00000000000000003009', '$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG', '447336936', 255);
