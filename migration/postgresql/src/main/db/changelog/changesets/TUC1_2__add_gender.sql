-- file: TUC1_2__add_gender.sql
ALTER TABLE bot_core.tg_users
    ADD COLUMN gender VARCHAR(10);
