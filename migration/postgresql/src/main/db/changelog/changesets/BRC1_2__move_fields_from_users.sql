-- file: BRC1_2__move_fields_from_users.sql
ALTER TABLE bot_core.birth_records
    ADD COLUMN timezone_id           BIGINT,
    ADD COLUMN latitude              DOUBLE PRECISION,
    ADD COLUMN longitude             DOUBLE PRECISION,
    ADD COLUMN notification_time     TIME,
    ADD COLUMN notification_time_utc TIME;

CREATE INDEX idx_br_notif_utc ON bot_core.birth_records(notification_time_utc);

DROP INDEX IF EXISTS bot_core.idx_tguser_notif_utc;

ALTER TABLE bot_core.tg_users
    DROP COLUMN IF EXISTS birth_date,
    DROP COLUMN IF EXISTS timezone_id,
    DROP COLUMN IF EXISTS latitude,
    DROP COLUMN IF EXISTS longitude,
    DROP COLUMN IF EXISTS notification_time,
    DROP COLUMN IF EXISTS notification_time_utc;
