--liquibase formatted sql

--changeset richard:003-create-user-profile
CREATE TABLE user_profile (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    agreed_to_terms BOOLEAN NOT NULL,

    CONSTRAINT chk_user_profile_agreed_to_terms CHECK (agreed_to_terms)
);
--rollback DROP TABLE user_profile;