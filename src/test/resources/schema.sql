--DROPPING constraint
ALTER TABLE IF EXISTS users
DROP CONSTRAINT IF EXISTS email_should_be_correct;

ALTER TABLE IF EXISTS users
DROP CONSTRAINT IF EXISTS email_should_be_unique;

ALTER TABLE IF EXISTS trackers
DROP
CONSTRAINT IF EXISTS fk_trackers_to_users;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS imei_should_be_unique;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS imei_should_be_correct;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS phone_number_should_be_unique;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS correct_phone_number;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS mileage_id_should_be_unique;

ALTER TABLE IF EXISTS trackers
DROP CONSTRAINT IF EXISTS fk_trackers_to_tracker_mileages;

ALTER TABLE IF EXISTS location
DROP
CONSTRAINT IF EXISTS fk_location_to_trackers;

ALTER TABLE IF EXISTS location
DROP
CONSTRAINT IF EXISTS fk_location_to_addresses;

ALTER TABLE IF EXISTS cities
DROP
CONSTRAINT IF EXISTS address_id_should_be_unique;

ALTER TABLE IF EXISTS parameters
DROP CONSTRAINT IF EXISTS parameter_name_should_be_correct;

ALTER TABLE IF EXISTS parameters
DROP
CONSTRAINT IF EXISTS fk_parameters_to_location;

ALTER TABLE IF EXISTS trackers_last_location
DROP
CONSTRAINT IF EXISTS fk_trackers_last_location_to_trackers;

ALTER TABLE IF EXISTS trackers_last_location
DROP
CONSTRAINT IF EXISTS fk_trackers_last_location_to_location;

ALTER TABLE IF EXISTS cities
DROP
CONSTRAINT IF EXISTS fk_cities_to_addresses;

--DROPPING tables
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trackers;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS parameters;
DROP TABLE IF EXISTS trackers_last_location;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS tracker_mileages;

--DROPPING SEQUENCES
DROP SEQUENCE IF EXISTS addresses_id_seq;
DROP SEQUENCE IF EXISTS location_id_seq;
DROP SEQUENCE IF EXISTS parameters_id_seq;

--DROPPING TYPES
DROP TYPE IF EXISTS user_type;
DROP TYPE IF EXISTS parameter_type;

CREATE
EXTENSION IF NOT EXISTS postgis;

CREATE TYPE user_type AS ENUM('USER', 'ADMIN');

CREATE TABLE users
(
    id                 SERIAL       PRIMARY KEY,
    email              VARCHAR(256) NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    role               user_type NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT email_should_be_correct CHECK (email ~ '[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+');

ALTER TABLE users
    ADD CONSTRAINT email_should_be_unique UNIQUE(email);

CREATE TABLE tracker_mileages(
	id SERIAL PRIMARY KEY,
	urban DOUBLE PRECISION NOT NULL,
	country DOUBLE PRECISION NOT NULL
);

CREATE TABLE trackers
(
    id                 SERIAL       PRIMARY KEY,
    imei               CHAR(20)     NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    phone_number       CHAR(9)      NOT NULL,
    user_id            INTEGER      NOT NULL,
    mileage_id        INTEGER      NOT NULL
);

ALTER TABLE trackers
    ADD CONSTRAINT fk_trackers_to_users FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE;

ALTER TABLE trackers
    ADD CONSTRAINT imei_should_be_unique UNIQUE(imei);

ALTER TABLE trackers
    ADD CONSTRAINT imei_should_be_correct CHECK (imei ~ '[0-9]{20}');

ALTER TABLE trackers
    ADD CONSTRAINT phone_number_should_be_unique UNIQUE(phone_number);

ALTER TABLE trackers
    ADD CONSTRAINT correct_phone_number CHECK (phone_number ~ '[0-9]{9}');

ALTER TABLE trackers
	ADD CONSTRAINT mileage_id_should_be_unique UNIQUE(mileage_id);

ALTER TABLE trackers
	ADD CONSTRAINT fk_trackers_to_tracker_mileages
		FOREIGN KEY (mileage_id) REFERENCES tracker_mileages(id);

CREATE TABLE addresses
(
    id           BIGSERIAL PRIMARY KEY,
    bounding_box GEOMETRY NOT NULL,
    center       GEOMETRY NOT NULL,
    city_name    VARCHAR(256) NOT NULL,
    country_name VARCHAR(256) NOT NULL,
    geometry     GEOMETRY NOT NULL
);

ALTER SEQUENCE addresses_id_seq INCREMENT 50;

CREATE INDEX ON addresses using GIST(geometry);

CREATE TABLE location
(
    id                     BIGSERIAL    PRIMARY KEY,
    date_time              TIMESTAMP(0) NOT NULL,
    latitude               DECIMAL      NOT NULL,
    longitude              DECIMAL      NOT NULL,
    speed                  DECIMAL      NOT NULL,
    course                 INTEGER      NOT NULL,
    altitude               INTEGER      NOT NULL,
    amount_of_satellites   INTEGER      NOT NULL,
    hdop    DECIMAL      NOT NULL,
    inputs                 INTEGER      NOT NULL,
    outputs                INTEGER      NOT NULL,
    analog_inputs          DOUBLE PRECISION[] NOT NULL,
    driver_key_code        VARCHAR(256) NOT NULL,
    tracker_id             INTEGER      NOT NULL,
    address_id             BIGINT       NOT NULL
);

ALTER SEQUENCE location_id_seq INCREMENT 50;

ALTER TABLE location
    ADD CONSTRAINT fk_location_to_trackers FOREIGN KEY (tracker_id) REFERENCES trackers (id)
        ON DELETE CASCADE;

ALTER TABLE location
    ADD CONSTRAINT fk_location_to_addresses FOREIGN KEY (address_id) REFERENCES addresses (id);

CREATE TYPE parameter_type AS ENUM('INTEGER', 'DOUBLE', 'STRING');

CREATE TABLE parameters
(
    id      BIGSERIAL      PRIMARY KEY,
    name    VARCHAR(256)   NOT NULL,
    type    parameter_type NOT NULL,
    value   VARCHAR(256)   NOT NULL,
    location_id BIGINT         NOT NULL
);

ALTER SEQUENCE parameters_id_seq INCREMENT 50;

ALTER TABLE parameters
    ADD CONSTRAINT fk_parameters_to_location
        FOREIGN KEY (location_id) REFERENCES location (id)
            ON DELETE CASCADE;

ALTER TABLE parameters
    ADD CONSTRAINT parameter_name_should_be_correct CHECK (char_length(name) != 0);

CREATE TABLE trackers_last_location
(
    id         SERIAL  PRIMARY KEY,
    tracker_id INTEGER NOT NULL UNIQUE,
    location_id    BIGINT UNIQUE
);

ALTER TABLE trackers_last_location
    ADD CONSTRAINT fk_trackers_last_location_to_trackers FOREIGN KEY (tracker_id)
        REFERENCES trackers (id)
        ON DELETE CASCADE;

ALTER TABLE trackers_last_location
    ADD CONSTRAINT fk_trackers_last_location_to_location FOREIGN KEY (location_id)
        REFERENCES location (id);

CREATE TABLE cities
(
    id                          BIGSERIAL PRIMARY KEY,
    address_id                  BIGINT UNIQUE NOT NULL
);

ALTER TABLE cities
    ADD CONSTRAINT fk_cities_to_addresses FOREIGN KEY (address_id)
        REFERENCES addresses (id)
        ON DELETE CASCADE;

ALTER TABLE cities
    ADD CONSTRAINT address_id_should_be_unique
        UNIQUE (address_id);

CREATE
OR REPLACE FUNCTION before_insert_tracker() RETURNS TRIGGER AS
'
    BEGIN
		INSERT INTO tracker_mileages(urban, country) VALUES(0, 0) RETURNING id INTO NEW.mileage_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_before_insert_tracker
    BEFORE INSERT
    ON trackers
    FOR EACH ROW
    EXECUTE PROCEDURE before_insert_tracker();

CREATE
OR REPLACE FUNCTION insert_tracker_last_location() RETURNS TRIGGER AS
'
    BEGIN
        INSERT INTO trackers_last_location(tracker_id)
        VALUES (NEW.id);
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_after_insert_tracker
    AFTER INSERT
    ON trackers
    FOR EACH ROW
    EXECUTE PROCEDURE insert_tracker_last_location();

CREATE
OR REPLACE FUNCTION update_tracker_last_location() RETURNS TRIGGER AS
'
    BEGIN
		UPDATE trackers_last_location
        SET location_id = NEW.id
        WHERE trackers_last_location.tracker_id = NEW.tracker_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_after_insert_location
    AFTER INSERT
    ON location
    FOR EACH ROW
    EXECUTE PROCEDURE update_tracker_last_location();