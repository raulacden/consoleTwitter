DROP TABLE IF EXISTS users, posts,follows;

CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    username VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS posts (
    id serial PRIMARY KEY,
    user_id int NOT NULL,
    text VARCHAR(280) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS follows (
    user_id int NOT NULL,
    user_id_followed int NOT NULL,
	PRIMARY KEY (user_id, user_id_followed),
	FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE,
	FOREIGN KEY (user_id_followed) REFERENCES users(id) ON UPDATE CASCADE
);