ALTER TABLE IF EXISTS trackers DROP CONSTRAINT IF EXISTS fk_trackers_to_users;
ALTER TABLE IF EXISTS trackers DROP CONSTRAINT IF EXISTS mileage_id_should_be_unique;
ALTER TABLE IF EXISTS trackers DROP CONSTRAINT IF EXISTS fk_trackers_to_mileages;
ALTER TABLE IF EXISTS addresses DROP CONSTRAINT IF EXISTS fk_addresses_to_cities;
ALTER TABLE IF EXISTS locations DROP CONSTRAINT IF EXISTS fk_locations_to_trackers;
ALTER TABLE IF EXISTS locations DROP CONSTRAINT IF EXISTS fk_locations_to_addresses;
ALTER TABLE IF EXISTS parameters DROP CONSTRAINT IF EXISTS fk_parameters_to_locations;
ALTER TABLE IF EXISTS tracker_last_locations DROP CONSTRAINT IF EXISTS fk_tracker_last_locations_to_trackers;
ALTER TABLE IF EXISTS tracker_last_locations DROP CONSTRAINT IF EXISTS fk_tracker_last_locations_to_locations;
ALTER TABLE IF EXISTS cities DROP CONSTRAINT IF EXISTS fk_cities_to_addresses;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trackers;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS parameters;
DROP TABLE IF EXISTS tracker_last_locations;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS mileages;

DROP SEQUENCE IF EXISTS addresses_id_seq;
DROP SEQUENCE IF EXISTS locations_id_seq;
DROP SEQUENCE IF EXISTS parameters_id_seq;

DROP TYPE IF EXISTS user_type;
DROP TYPE IF EXISTS parameter_type;

CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TYPE user_type AS ENUM('USER', 'ADMIN');
CREATE TYPE parameter_type AS ENUM('INTEGER', 'DOUBLE', 'STRING');

CREATE TABLE users
(
    id                 SERIAL       PRIMARY KEY,
    email              VARCHAR(256) NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    role               user_type    NOT NULL,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE UNIQUE INDEX users_email_unique_index ON users (email) WHERE is_deleted = FALSE;

CREATE TABLE mileages
(
	id           SERIAL           PRIMARY KEY,
	urban        DOUBLE PRECISION NOT NULL,
	country      DOUBLE PRECISION NOT NULL,
    is_deleted   BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP        NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP        NOT NULL DEFAULT NOW()
);

CREATE TABLE trackers
(
    id                 SERIAL       PRIMARY KEY,
    imei               CHAR(20)     NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    phone_number       CHAR(9)      NOT NULL,
    user_id            INTEGER      NOT NULL,
    mileage_id         INTEGER      NOT NULL,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT NOW()
);
ALTER TABLE trackers ADD CONSTRAINT fk_trackers_to_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE trackers ADD CONSTRAINT mileage_id_should_be_unique UNIQUE(mileage_id);
ALTER TABLE trackers ADD CONSTRAINT fk_trackers_to_mileages FOREIGN KEY (mileage_id) REFERENCES mileages(id);
CREATE UNIQUE INDEX trackers_imei_unique_index ON trackers (imei) WHERE is_deleted = FALSE;
CREATE UNIQUE INDEX trackers_phone_number_unique_index ON trackers (phone_number) WHERE is_deleted = FALSE;

CREATE TABLE addresses
(
    id           BIGSERIAL    PRIMARY KEY,
    bounding_box GEOMETRY     NOT NULL,
    center       GEOMETRY     NOT NULL,
    geometry     GEOMETRY     NOT NULL,
    city_id      BIGINT       NOT NULL,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);
ALTER TABLE addresses ADD CONSTRAINT fk_addresses_to_cities FOREIGN KEY (city_id) REFERENCES cities (id);
ALTER SEQUENCE addresses_id_seq INCREMENT 50;
CREATE INDEX ON addresses using GIST(geometry);

CREATE TABLE locations
(
    id                     BIGSERIAL          PRIMARY KEY,
    date_time              TIMESTAMP(0)       NOT NULL,
    latitude               DECIMAL            NOT NULL,
    longitude              DECIMAL            NOT NULL,
    speed                  DECIMAL            NOT NULL,
    course                 INTEGER            NOT NULL,
    altitude               INTEGER            NOT NULL,
    satellite_count        INTEGER            NOT NULL,
    hdop                   DECIMAL            NOT NULL,
    inputs                 INTEGER            NOT NULL,
    outputs                INTEGER            NOT NULL,
    analog_inputs          DOUBLE PRECISION[] NOT NULL,
    driver_key_code        VARCHAR(256)       NOT NULL,
    tracker_id             INTEGER            NOT NULL,
    address_id             BIGINT             NOT NULL,
    is_deleted             BOOLEAN            NOT NULL DEFAULT FALSE,
    created_at             TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP          NOT NULL DEFAULT NOW()
);
ALTER SEQUENCE locations_id_seq INCREMENT 50;
ALTER TABLE locations ADD CONSTRAINT fk_locations_to_trackers FOREIGN KEY (tracker_id) REFERENCES trackers (id);
ALTER TABLE locations ADD CONSTRAINT fk_locations_to_addresses FOREIGN KEY (address_id) REFERENCES addresses (id);

CREATE TABLE parameters
(
    id          BIGSERIAL      PRIMARY KEY,
    name        VARCHAR(256)   NOT NULL,
    type        parameter_type NOT NULL,
    value       VARCHAR(256)   NOT NULL,
    location_id BIGINT         NOT NULL,
    is_deleted  BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);
ALTER SEQUENCE parameters_id_seq INCREMENT 50;
ALTER TABLE parameters ADD CONSTRAINT fk_parameters_to_locations FOREIGN KEY (location_id) REFERENCES locations (id);

CREATE TABLE tracker_last_locations
(
    id          SERIAL    PRIMARY KEY,
    tracker_id  INTEGER   NOT NULL UNIQUE,
    location_id BIGINT    UNIQUE,
    is_deleted  BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);
ALTER TABLE tracker_last_locations ADD CONSTRAINT fk_tracker_last_locations_to_trackers FOREIGN KEY (tracker_id) REFERENCES trackers (id);
ALTER TABLE tracker_last_locations ADD CONSTRAINT fk_tracker_last_locations_to_locations FOREIGN KEY (location_id) REFERENCES locations (id);

CREATE TABLE cities
(
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(256) NOT NULL,
    country_name VARCHAR(256) NOT NULL,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);
ALTER SEQUENCE cities_id_seq INCREMENT 50;
ALTER TABLE cities ADD CONSTRAINT fk_cities_to_addresses FOREIGN KEY (address_id) REFERENCES addresses (id);
CREATE UNIQUE INDEX cities_name_unique_index ON cities (name, country_name) WHERE is_deleted = FALSE;

CREATE OR REPLACE FUNCTION insert_initial_mileage() RETURNS TRIGGER AS
'
    BEGIN
		INSERT INTO mileages(urban, country) VALUES(0, 0) RETURNING id INTO NEW.mileage_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_insert_initial_mileage
BEFORE INSERT ON trackers FOR EACH ROW
EXECUTE PROCEDURE insert_initial_mileage();

CREATE OR REPLACE FUNCTION insert_tracker_last_location() RETURNS TRIGGER AS
'
    BEGIN
        INSERT INTO tracker_last_locations(tracker_id) VALUES (NEW.id);
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_after_insert_tracker
AFTER INSERT ON trackers FOR EACH ROW
EXECUTE PROCEDURE insert_tracker_last_location();

CREATE OR REPLACE FUNCTION update_tracker_last_location() RETURNS TRIGGER AS
'
    BEGIN
		UPDATE tracker_last_locations SET location_id = NEW.id WHERE tracker_id = NEW.tracker_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_after_insert_location
AFTER INSERT ON locations FOR EACH ROW
EXECUTE PROCEDURE update_tracker_last_locations();