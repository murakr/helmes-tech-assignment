--liquibase formatted sql

--changeset richard:001-create-sector
CREATE TABLE sector (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id INTEGER,
    sort_order INTEGER NOT NULL,

    CONSTRAINT fk_sector_parent FOREIGN KEY (parent_id) REFERENCES sector(id)
);
--rollback DROP TABLE sector;