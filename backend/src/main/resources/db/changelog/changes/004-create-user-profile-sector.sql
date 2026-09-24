--liquibase formatted sql

--changeset richard:004-create-user-profile-sector
CREATE TABLE user_profile_sector (
    user_profile_id BIGINT NOT NULL,
    sector_id INTEGER NOT NULL,

    CONSTRAINT pk_user_profile_sector PRIMARY KEY (user_profile_id, sector_id),
    CONSTRAINT fk_user_profile_sector_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_profile_sector_sector FOREIGN KEY (sector_id) REFERENCES sector (id)
);
--rollback DROP TABLE user_profile_sector;