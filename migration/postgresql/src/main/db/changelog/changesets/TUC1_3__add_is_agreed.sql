-- file: TUC1_3__add_is_agreed.sql
ALTER TABLE bot_core.tg_users
    ADD COLUMN is_agreed BOOLEAN DEFAULT FALSE;
