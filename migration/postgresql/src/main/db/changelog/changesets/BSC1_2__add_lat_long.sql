-- file: BSC1_2__add_lat_long.sql
ALTER TABLE bot_core.bot_sessions
    ADD COLUMN temp_latitude    DOUBLE PRECISION,
    ADD COLUMN temp_longitude   DOUBLE PRECISION;
