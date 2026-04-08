-- file: BRC1_1__add_is_active.sql
ALTER TABLE bot_core.birth_records
    ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
