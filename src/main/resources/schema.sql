drop table if exists users cascade;
drop table if exists items cascade;
drop table if exists bookings cascade;
--drop table if exists requests cascade;
drop table if exists comments cascade;


CREATE TABLE IF NOT EXISTS users (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(100) NOT NULL, email varchar(320) NOT NULL, CONSTRAINT email_unique UNIQUE (email));

-- CREATE TABLE IF NOT EXISTS requests (
--     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--     description varchar(320), requestor_id BIGINT, CONSTRAINT fk_requestor FOREIGN KEY (requestor_id)
--     REFERENCES users(id) ON DELETE CASCADE
--     );

CREATE TABLE IF NOT EXISTS items (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(100), description varchar(320), is_available BOOLEAN, owner_id BIGINT, request_id BIGINT, CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE);
--     CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES requests(id)

CREATE TABLE IF NOT EXISTS bookings (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, start_date TIMESTAMP WITHOUT TIME ZONE, end_date TIMESTAMP WITHOUT TIME ZONE, item_id BIGINT, booker_id BIGINT, status varchar, CONSTRAINT fk_items FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE, CONSTRAINT fk_bookers FOREIGN KEY (booker_id) REFERENCES users(id) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS comments (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, text varchar(320), item_id BIGINT, author_id BIGINT, created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP, CONSTRAINT fk_items_comments FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE, CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE);

