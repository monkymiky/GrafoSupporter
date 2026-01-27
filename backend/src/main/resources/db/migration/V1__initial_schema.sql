-- Initial schema aligned with JPA entities (snake_case, PostgreSQL).

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255),
    name VARCHAR(255),
    picture_url VARCHAR(255),
    google_id VARCHAR(255),
    custom_username VARCHAR(255),
    created_at TIMESTAMP,
    last_login_at TIMESTAMP
);

CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    writer VARCHAR(255),
    publication_year INTEGER,
    editor VARCHAR(255),
    isbn VARCHAR(255)
);

CREATE TABLE sign (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    temperamento VARCHAR(255),
    tipo VARCHAR(255)
);

CREATE TABLE combination (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(512),
    short_description VARCHAR(1024),
    long_description VARCHAR(2048),
    original_text_condition VARCHAR(2048),
    image_path VARCHAR(512),
    user_id BIGINT NOT NULL REFERENCES app_user(id),
    book_id BIGINT REFERENCES book(id)
);

CREATE TABLE valuated_sign (
    id BIGSERIAL PRIMARY KEY,
    sign_id BIGINT REFERENCES sign(id),
    combination_id BIGINT REFERENCES combination(id),
    max INTEGER,
    min INTEGER,
    classification VARCHAR(255),
    is_optional BOOLEAN,
    sign_order INTEGER
);

CREATE TABLE comment (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(2000) NOT NULL,
    author_id BIGINT NOT NULL REFERENCES app_user(id),
    combination_id BIGINT NOT NULL REFERENCES combination(id),
    parent_comment_id BIGINT REFERENCES comment(id),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE vote (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id),
    combination_id BIGINT NOT NULL REFERENCES combination(id),
    vote_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    CONSTRAINT uq_vote_user_combination UNIQUE (user_id, combination_id)
);

CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    recipient_id BIGINT NOT NULL REFERENCES app_user(id),
    comment_id BIGINT REFERENCES comment(id),
    combination_id BIGINT REFERENCES combination(id),
    read BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE contact_feedback (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    subject VARCHAR(500) NOT NULL,
    message VARCHAR(5000) NOT NULL,
    user_agent VARCHAR(1000),
    page_url VARCHAR(1000),
    created_at TIMESTAMP NOT NULL
);
