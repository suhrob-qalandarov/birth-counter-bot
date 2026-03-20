set search_path to bot_core;

ALTER TABLE bot_core.tg_users
    ADD COLUMN IF NOT EXISTS birth_date DATE;
