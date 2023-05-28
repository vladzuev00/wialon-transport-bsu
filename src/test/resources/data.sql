INSERT INTO users(id, email, encrypted_password, role)
VALUES(255, 'vladzuev.00@mail.ru', '$2a$10$yeSV48OoEiRImSrinf8sOuxld6iQzHH8BFNHYd7LhY6yzUnhSTOie', 'USER');

INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id)
VALUES(255, '11112222333344445555', 'password', '447336934', 255);
